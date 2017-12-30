package org.yajcms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yajcms.beans.EntitiesStorage;
import org.yajcms.beans.EntitiesStorageMongoDBImpl;
import org.yajcms.beans.EntityIdGenerator;
import org.yajcms.beans.SimpleIdGenerator;
import org.yajcms.core.blobs.BlobStorageApi;
import org.yajcms.core.blobs.PostgresBlobStorageApi;

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

    @Bean
    BlobStorageApi blobStorageApi() {
        return new PostgresBlobStorageApi();
    }
}
