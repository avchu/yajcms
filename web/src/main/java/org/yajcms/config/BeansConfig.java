package org.yajcms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yajcms.beans.EntitiesStorage;
import org.yajcms.beans.EntitiesStorageMongoDBImpl;
import org.yajcms.beans.EntityIdGenerator;
import org.yajcms.beans.SimpleIdGenerator;

@Configuration
public class BeansConfig {
    @Bean
    EntitiesStorage entitiesStorage() {
        return new EntitiesStorageMongoDBImpl();
    }

    @Bean
    EntityIdGenerator entityIdGenerator() {
        return new SimpleIdGenerator();
    }
}
