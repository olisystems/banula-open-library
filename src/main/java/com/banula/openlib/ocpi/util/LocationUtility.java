package com.banula.openlib.ocpi.util;

import com.banula.openlib.ocpi.mapper.LocationMapper;
import com.banula.openlib.ocpi.model.Location;
import com.banula.openlib.ocpi.model.dto.LocationDTO;
import com.banula.openlib.ocpi.model.vo.Connector;
import com.banula.openlib.ocpi.model.vo.EVSE;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public abstract class LocationUtility {

    @Getter
    @Setter
    protected MongoTemplate mongoTemplate;

    /*
     * As per documentation:
     * 1 - When evseUid and connectorId are null we return a Location object
     * 2 - when evseUid is nonNull and connectorId is Null we return an EVSE object
     * 3 - when evseUid and connectorId are both nonNull we return a Connector
     * object
     */
    public Object findLocations(String countryCode, String party_id, String locationId, String evseUid,
            String connectorId, String collectionName) {
        Query query = createQueryForLocationFetching(countryCode, party_id, locationId);
        Location location = mongoTemplate.findOne(query, Location.class, collectionName);
        if (location != null && evseUid != null) {
            EVSE evse = filterEvsesBasedOnEvseId(evseUid, location);
            if (evse != null && connectorId != null) {
                return filterConnectorsByConnectorId(connectorId, evse);
            }
            return evse;
        }
        return LocationMapper.toLocationDTO(location);
    }

    public List<LocationDTO> findLocations(LocalDateTime dateFrom, LocalDateTime dateTo, Integer offset, Integer limit,
            String collectionName) {
        Pageable pageable = PageRequest.of(offset, limit);
        Query query = createQueryForLocationFetching(dateFrom, dateTo);
        List<LocationDTO> locationDTOS = this.getMongoTemplate().find(query, LocationDTO.class, collectionName);
        return PageableExecutionUtils.getPage(
                locationDTOS,
                pageable,
                () -> this.getMongoTemplate().count(query, LocationDTO.class)).getContent();
    }

    public List<LocationDTO> findLocationsByParty(String countryCode, String party_id, String collectionName) {
        Query query = createQueryForLocationFetching(countryCode, party_id);
        return this.getMongoTemplate().find(query, LocationDTO.class, collectionName);
    }

    private Query createQueryForLocationFetching(LocalDateTime dateFrom, LocalDateTime dateTo) {
        Query query = new Query();
        if (dateFrom != null && dateTo == null) {
            query.addCriteria(Criteria.where("lastUpdated").gte(dateFrom));
        }
        if (dateFrom == null && dateTo != null) {
            query.addCriteria(Criteria.where("lastUpdated").lte(dateTo));
        }
        if (dateFrom != null && dateTo != null) {
            query.addCriteria(Criteria.where("lastUpdated").gte(dateFrom).lte(dateTo));
        }
        return query;
    }

    @Nullable
    private static Connector filterConnectorsByConnectorId(String connectorId, EVSE evse) {
        if (evse.getConnectors() == null || evse.getConnectors().isEmpty()) {
            return null;
        }
        return evse.getConnectors().stream()
                .filter(connectorIteration -> connectorIteration.getId().equals(connectorId))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    private static EVSE filterEvsesBasedOnEvseId(String evseUid, Location location) {
        if (location.getEvses() == null || location.getEvses().isEmpty()) {
            return null;
        }
        return location.getEvses().stream()
                .filter(evseIteration -> evseIteration.getUid().equals(evseUid))
                .findFirst()
                .orElse(null);
    }

    private Query createQueryForLocationFetching(String countryCode, String party_id, String locationId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("countryCode").is(countryCode));
        query.addCriteria(Criteria.where("partyId").is(party_id));
        query.addCriteria(Criteria.where("id").is(locationId));
        return query;
    }

    private Query createQueryForLocationFetching(String countryCode, String party_id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("countryCode").is(countryCode));
        query.addCriteria(Criteria.where("partyId").is(party_id));
        return query;
    }

    public static int indexOf(List<EVSE> evses, EVSE evse) {
        for (int i = 0; i < evses.size(); i++) {
            if (evses.get(i).getUid().equals(evse.getUid())) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(List<Connector> evses, Connector evse) {
        for (int i = 0; i < evses.size(); i++) {
            if (evses.get(i).getId().equals(evse.getId())) {
                return i;
            }
        }
        return -1;
    }

    private Query createQueryForLocationFetching(String locationId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(locationId));
        return query;
    }

    public String findConnector(String locationId, String evseUid, String connectorId, String collectionName) {
        try {
            if (evseUid != null && connectorId == null) {
                return evseUid + "_1";
            }
            if (connectorId != null && evseUid != null) {
                return connectorId;
            } else {
                Query query = createQueryForLocationFetching(locationId);
                LocationDTO locationDTO = this.getMongoTemplate().findOne(query, LocationDTO.class, collectionName);
                return locationDTO == null ? null : locationDTO.getEvses().get(0).getConnectors().get(0).getId();
            }
        } catch (Exception e) {
            String errorMessage = "Could not find the required connectorId, error message: " + e.getLocalizedMessage();
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }

    }

    public static EVSE getEvseInLocation(Location location, String evseUid) {
        if (location.getEvses() != null) {
            for (EVSE evse : location.getEvses()) {
                if (evse.getUid().equals(evseUid)) {
                    return evse;
                }
            }
        }
        return null;
    }

    public static Connector getConnectorInEvse(EVSE evse, String connectorId) {
        if (evse.getConnectors() != null) {
            for (Connector connector : evse.getConnectors()) {
                if (connector.getId().equals(connectorId)) {
                    return connector;
                }
            }
        }
        return null;
    }

}
