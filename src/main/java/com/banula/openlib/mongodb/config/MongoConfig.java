package com.banula.openlib.mongodb.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import org.springframework.core.convert.converter.Converter;

/**
 * MongoDB configuration to ensure all LocalDateTime values are stored and
 * retrieved as UTC,
 * regardless of the system's default timezone.
 * 
 * This configuration should be imported by all services using MongoDB to ensure
 * consistent
 * timestamp handling across the platform.
 */
@Configuration
@ConditionalOnClass(MongoTemplate.class)
public class MongoConfig {

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(List.of(
                new LocalDateTimeToDateConverter(),
                new DateToLocalDateTimeConverter()));
    }

    /**
     * Configures the MongoDB converter with custom settings.
     * 
     * <p>
     * <b>IMPORTANT:</b> This configuration disables the default '_class' type
     * metadata
     * by setting {@code DefaultMongoTypeMapper(null)}. This is safe ONLY because:
     * <ul>
     * <li>Each entity type is stored in its own dedicated collection</li>
     * <li>No polymorphic types are stored in the same collection</li>
     * <li>Repositories are typed to specific entity classes</li>
     * </ul>
     * 
     * <p>
     * <b>WARNING:</b> If you plan to store polymorphic hierarchies (e.g., parent
     * and child
     * classes) in the same collection, or use interface/abstract types as field
     * types,
     * you MUST remove the {@code setTypeMapper(new DefaultMongoTypeMapper(null))}
     * line
     * to enable type discrimination, otherwise deserialization will fail or lose
     * subclass data.
     */
    @Bean
    public MappingMongoConverter mappingMongoConverter(
            MongoDatabaseFactory mongoDatabaseFactory,
            MongoMappingContext mongoMappingContext,
            MongoCustomConversions customConversions) {

        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        converter.setCustomConversions(customConversions);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        converter.afterPropertiesSet();
        return converter;
    }

    /**
     * Converts LocalDateTime to Date using UTC timezone.
     * This ensures that OCPI timestamps are always stored as UTC in MongoDB.
     */
    static class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {
        @Override
        public Date convert(LocalDateTime source) {
            return Date.from(source.atZone(ZoneOffset.UTC).toInstant());
        }
    }

    /**
     * Converts Date to LocalDateTime using UTC timezone.
     * This ensures that OCPI timestamps are always read as UTC from MongoDB.
     */
    static class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {
        @Override
        public LocalDateTime convert(Date source) {
            return LocalDateTime.ofInstant(source.toInstant(), ZoneOffset.UTC);
        }
    }
}
