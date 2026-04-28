package com.banula.openlib.mongodb.util;

import com.banula.openlib.ocpi.custom.tenantOcpiObjects.mongo.*;
import com.banula.openlib.ocpi.mapper.GenericMapper;
import com.banula.openlib.ocpi.model.*;
import com.banula.openlib.ocpi.model.mongo.MongoCDR;
import com.banula.openlib.ocpi.model.mongo.MongoChargingSession;
import com.banula.openlib.ocpi.model.mongo.MongoLocation;
import com.banula.openlib.ocpi.model.mongo.MongoTariff;
import com.banula.openlib.ocpi.model.mongo.MongoToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ConditionalOnClass(MongoTemplate.class)
@Component
public class GenericMongoMapper extends GenericMapper {

    @Autowired
    private MongoTemplate mongoTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Core mongo conversions with smart upsert
    public <T, M> M toMongo(T entity, Class<M> mongoClass) {
        try {
            M mongoEntity = mongoClass.getDeclaredConstructor().newInstance();
            MongoBeanUtils.copyProperties(entity, mongoEntity);

            // Get compound index annotation
            CompoundIndex compoundIndex = getCompoundIndexAnnotation(mongoClass);
            validateUniqueIndex(compoundIndex);

            // Parse business key fields from index definition
            List<String> businessKeyFields = parseIndexFieldNames(compoundIndex.def());

            // Find existing document using business key fields
            M existingDocument = findExistingDocument(mongoEntity, mongoClass, businessKeyFields);

            // If existing document found, copy its mongoId
            if (existingDocument != null) {
                Object existingId = getFieldIfExists(existingDocument, "mongoId");
                if (existingId != null) {
                    setFieldIfExists(mongoEntity, "mongoId", existingId);
                }
            }

            return mongoEntity;
        } catch (Exception e) {
            throw new RuntimeException("Error converting entity to mongo entity: " + e.getMessage(), e);
        }
    }

    public <T, M> T toEntity(M mongoEntity, Class<T> entityClass) {
        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            MongoBeanUtils.copyProperties(mongoEntity, entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error converting mongo entity to entity: " + e.getMessage(), e);
        }
    }

    // Composite mongo conversions
    public <M, D, T> D mongoToDTO(M mongoEntity, Class<T> entityClass, Class<D> dtoClass) {
        T entity = toEntity(mongoEntity, entityClass);
        return toDTO(entity, dtoClass);
    }

    public <D, M, T> M dtoToMongo(D dto, Class<T> entityClass, Class<M> mongoClass) {
        T entity = fromDTO(dto, entityClass);
        return toMongo(entity, mongoClass);
    }

    // Mongo list variants
    public <T, M> List<M> toMongoList(List<T> entities, Class<M> mongoClass) {
        List<M> mongoEntities = new ArrayList<>();
        for (T entity : entities) {
            mongoEntities.add(toMongo(entity, mongoClass));
        }
        return mongoEntities;
    }

    public <T, M> List<T> toEntityList(List<M> mongoEntities, Class<T> entityClass) {
        List<T> entities = new ArrayList<>();
        for (M mongoEntity : mongoEntities) {
            entities.add(toEntity(mongoEntity, entityClass));
        }
        return entities;
    }

    public <M, D, T> List<D> mongoListToDTO(List<M> mongoEntities, Class<T> entityClass, Class<D> dtoClass) {
        List<D> dtos = new ArrayList<>();
        for (M mongoEntity : mongoEntities) {
            dtos.add(mongoToDTO(mongoEntity, entityClass, dtoClass));
        }
        return dtos;
    }

    public <D, M, T> List<M> dtoListToMongo(List<D> dtos, Class<T> entityClass, Class<M> mongoClass) {
        List<M> mongoEntities = new ArrayList<>();
        for (D dto : dtos) {
            mongoEntities.add(dtoToMongo(dto, entityClass, mongoClass));
        }
        return mongoEntities;
    }

    // Specific model mongo methods

    public <T> MongoCDR cdrToMongo(T entity) {
        return toMongo(entity, MongoCDR.class);
    }

    public <D> MongoCDR cdrDtoToMongo(D dto) {
        MongoCDR mongo = dtoToMongo(dto, CDR.class, MongoCDR.class);
        MongoBeanUtils.finalizeMongoId(mongo);
        return mongo;
    }

    public <D> MongoTenantCDR cdrDtoToMongoTenant(D dto, String tenant) {
        MongoTenantCDR mongo = dtoToMongo(dto, CDR.class, MongoTenantCDR.class);
        mongo.setTenant(tenant);
        MongoBeanUtils.finalizeMongoId(mongo);
        return mongo;
    }

    public <D> MongoTenantCDR cdrToMongoTenant(D entity, String tenant) {
        MongoTenantCDR mongo = toMongo(entity, MongoTenantCDR.class);
        mongo.setTenant(tenant);
        MongoBeanUtils.finalizeMongoId(mongo);
        return mongo;
    }

    public <T> MongoLocation locationToMongo(T entity) {
        return toMongo(entity, MongoLocation.class);
    }

    public <D> MongoLocation locationDtoToMongo(D dto) {
        MongoLocation mongo = dtoToMongo(dto, Location.class, MongoLocation.class);
        MongoBeanUtils.finalizeMongoId(mongo);
        return mongo;
    }

    public <D> MongoTenantLocation locationDtoToMongoTenant(D dto, String tenant) {
        MongoTenantLocation mongo = dtoToMongo(dto, Location.class, MongoTenantLocation.class);
        mongo.setTenant(tenant);
        MongoBeanUtils.finalizeMongoId(mongo);
        return mongo;
    }

    public <D> MongoTenantLocation locationToMongoTenant(D entity, String tenant) {
        MongoTenantLocation mongo = toMongo(entity, MongoTenantLocation.class);
        mongo.setTenant(tenant);
        MongoBeanUtils.finalizeMongoId(mongo);
        return mongo;
    }

    public <T> MongoTariff tariffToMongo(T entity) {
        return toMongo(entity, MongoTariff.class);
    }

    public <D> MongoTariff tariffDtoToMongo(D dto) {
        MongoTariff mongo = dtoToMongo(dto, Tariff.class, MongoTariff.class);
        MongoBeanUtils.finalizeMongoId(mongo);
        return mongo;
    }

    public <D> MongoTenantTariff tariffDtoToMongoTenant(D dto, String tenant) {
        MongoTenantTariff mongo = dtoToMongo(dto, Tariff.class, MongoTenantTariff.class);
        mongo.setTenant(tenant);
        MongoBeanUtils.finalizeMongoId(mongo);
        return mongo;
    }

    public <D> MongoTenantTariff tariffToMongoTenant(D entity, String tenant) {
        MongoTenantTariff mongo = toMongo(entity, MongoTenantTariff.class);
        mongo.setTenant(tenant);
        MongoBeanUtils.finalizeMongoId(mongo);
        return mongo;
    }

    public <T> MongoToken tokenToMongo(T entity) {
        return toMongo(entity, MongoToken.class);
    }

    public <D> MongoToken tokenDtoToMongo(D dto) {
        MongoToken mongo = dtoToMongo(dto, Token.class, MongoToken.class);
        MongoBeanUtils.finalizeMongoId(mongo);
        return mongo;
    }

    public <D> MongoTenantToken tokenDtoToMongoTenant(D dto, String tenant) {
        MongoTenantToken mongo = dtoToMongo(dto, Token.class, MongoTenantToken.class);
        mongo.setTenant(tenant);
        MongoBeanUtils.finalizeMongoId(mongo);
        return mongo;
    }

    public <D> MongoTenantToken tokenToMongoTenant(D entity, String tenant) {
        MongoTenantToken mongo = toMongo(entity, MongoTenantToken.class);
        mongo.setTenant(tenant);
        MongoBeanUtils.finalizeMongoId(mongo);
        return mongo;
    }

    public <T> MongoChargingSession sessionToMongo(T entity) {
        return toMongo(entity, MongoChargingSession.class);
    }

    public <D> MongoChargingSession sessionDtoToMongo(D dto) {
        MongoChargingSession mongo = dtoToMongo(dto, ChargingSession.class, MongoChargingSession.class);
        MongoBeanUtils.finalizeMongoId(mongo);
        return mongo;
    }

    public <D> MongoTenantChargingSession sessionDtoToMongoTenant(D dto, String tenant) {
        MongoTenantChargingSession mongo = dtoToMongo(dto, ChargingSession.class, MongoTenantChargingSession.class);
        mongo.setTenant(tenant);
        MongoBeanUtils.finalizeMongoId(mongo);
        return mongo;
    }

    public <D> MongoTenantChargingSession sessionToMongoTenant(D entity, String tenant) {
        MongoTenantChargingSession mongo = toMongo(entity, MongoTenantChargingSession.class);
        mongo.setTenant(tenant);
        MongoBeanUtils.finalizeMongoId(mongo);
        return mongo;
    }

    // Internal helper methods
    private CompoundIndex getCompoundIndexAnnotation(Class<?> mongoClass) {
        CompoundIndex annotation = mongoClass.getAnnotation(CompoundIndex.class);
        if (annotation == null) {
            throw new IllegalStateException(
                    "MongoEntity class " + mongoClass.getSimpleName() +
                            " must have @CompoundIndex annotation with unique=true");
        }
        return annotation;
    }

    private void validateUniqueIndex(CompoundIndex compoundIndex) {
        if (!compoundIndex.unique()) {
            throw new IllegalStateException(
                    "@CompoundIndex must have unique=true for smart upsert logic to work");
        }
    }

    private List<String> parseIndexFieldNames(String indexDefinition) {
        try {
            // Convert MongoDB index def format to valid JSON
            // Example: "{'countryCode': 1, 'partyId': 1, 'id': 1}" -> {"countryCode": 1,
            // "partyId": 1, "id": 1}
            String jsonString = indexDefinition.replace("'", "\"");

            // Parse JSON to extract field names
            @SuppressWarnings("unchecked")
            Map<String, Object> indexMap = objectMapper.readValue(jsonString, Map.class);

            return new ArrayList<>(indexMap.keySet());
        } catch (Exception e) {
            throw new RuntimeException("Error parsing index definition: " + indexDefinition, e);
        }
    }

    private <M> M findExistingDocument(M mongoEntity, Class<M> mongoClass, List<String> businessKeyFields) {
        Query query = buildBusinessKeyQuery(mongoEntity, businessKeyFields);
        if (query == null) {
            return null;
        }
        return mongoTemplate.findOne(query, mongoClass);
    }

    private Query buildBusinessKeyQuery(Object mongoEntity, List<String> businessKeyFields) {
        if (businessKeyFields == null || businessKeyFields.isEmpty()) {
            return null;
        }

        Criteria criteria = new Criteria();
        List<Criteria> criteriaList = new ArrayList<>();

        for (String fieldName : businessKeyFields) {
            Object fieldValue = getFieldIfExists(mongoEntity, fieldName);
            if (fieldValue == null) {
                return null;
            }
            criteriaList.add(Criteria.where(fieldName).is(fieldValue));
        }

        criteria.andOperator(criteriaList.toArray(new Criteria[0]));
        return new Query(criteria);
    }

    private void setFieldIfExists(Object target, String fieldName, Object value) {
        try {
            Field field = findField(target.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(target, value);
            }
        } catch (Exception e) {
            // Gracefully skip if field doesn't exist or can't be set
        }
    }

    private Object getFieldIfExists(Object source, String fieldName) {
        try {
            Field field = findField(source.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(source);
            }
        } catch (Exception e) {
            // Return null if field doesn't exist or can't be accessed
        }
        return null;
    }

    private Field findField(Class<?> clazz, String fieldName) {
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }
}
