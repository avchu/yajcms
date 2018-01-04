package org.yajcms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yajcms.beans.entities.blobs.FileApi;
import org.yajcms.beans.entities.blobs.FileApiImpl;
import org.yajcms.beans.entities.nosql.BasicEntityPreAndPostProcessorImpl;
import org.yajcms.beans.entities.nosql.EntitiesStorage;
import org.yajcms.beans.entities.nosql.EntitiesStorageMongoDBImpl;
import org.yajcms.beans.entities.nosql.EntityPreAndPostProcessor;
import org.yajcms.beans.generators.EntityIdGenerator;
import org.yajcms.beans.generators.SimpleIdGenerator;
import org.yajcms.beans.pipeline.BasicEntityDao;
import org.yajcms.beans.pipeline.EntitiesDao;
import org.yajcms.beans.entities.blobs.BlobStorageApi;
import org.yajcms.beans.entities.blobs.PostgresBlobStorageApi;

@Configuration
public class BeansConfig {
    @Bean
    protected EntitiesStorage entitiesStorage() {
        return new EntitiesStorageMongoDBImpl();
    }

    @Bean
    protected EntityIdGenerator entityIdGenerator() {
        return new SimpleIdGenerator();
    }

    @Bean
    protected BlobStorageApi blobStorageApi() {
        return new PostgresBlobStorageApi();
    }

    @Bean
    protected EntityPreAndPostProcessor entityPreAndPostProcessor() {
        return new BasicEntityPreAndPostProcessorImpl();
    }

    @Bean
    protected EntitiesDao entitiesPipeline() {
        return new BasicEntityDao();
    }

    @Bean
    protected FileApi fileApi() {
        return new FileApiImpl();
    }
}
