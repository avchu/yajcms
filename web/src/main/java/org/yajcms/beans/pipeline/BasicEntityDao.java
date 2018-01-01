package org.yajcms.beans.pipeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.yajcms.beans.EntitiesStorage;
import org.yajcms.beans.EntityPreAndPostProcessor;
import org.yajcms.core.Entity;

import java.util.List;
import java.util.Optional;

public class BasicEntityDao implements EntitiesDao {
    private EntitiesStorage entitiesStorage;
    private EntityPreAndPostProcessor entityPreAndPostProcessor;

    @Autowired
    public void setEntitiesStorage(EntitiesStorage entitiesStorage) {
        this.entitiesStorage = entitiesStorage;
    }

    @Autowired
    public void setEntityPreAndPostProcessor(EntityPreAndPostProcessor entityPreAndPostProcessor) {
        this.entityPreAndPostProcessor = entityPreAndPostProcessor;
    }

    @Override
    public Entity storeEntity(Entity entity) {
        entity = entityPreAndPostProcessor.prePut(entity);
        entity = entityPreAndPostProcessor.contextWork(entity);
        entity = entitiesStorage.storeEntity(entity);
        entity = entityPreAndPostProcessor.postPut(entity);
        return entity;
    }

    @Override
    public List<Entity> getAllByKey(String key) {
        return entitiesStorage.getAllByKey(key);
    }

    @Override
    public void delete(Entity entity) {
        entitiesStorage.delete(entity);
    }

    @Override
    public Optional<Entity> getByKey(String key, Object id) {
        return entitiesStorage.getByKey(key, id);
    }
}
