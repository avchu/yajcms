package org.yajcms.beans.pipeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.yajcms.beans.entities.Entity;
import org.yajcms.beans.entities.nosql.EntitiesStorage;
import org.yajcms.beans.entities.nosql.EntityPreAndPostProcessor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BasicEntityDao implements EntitiesDao {
    private EntitiesStorage entitiesStorage;
    private EntityPreAndPostProcessor entityPreAndPostProcessor;
    private QueryLanguageProcessor queryLanguageProcessor;

    @Autowired
    public void setQueryLanguageProcessor(QueryLanguageProcessor queryLanguageProcessor) {
        this.queryLanguageProcessor = queryLanguageProcessor;
    }

    @Autowired
    public void setEntitiesStorage(EntitiesStorage entitiesStorage) {
        this.entitiesStorage = entitiesStorage;
    }

    @Autowired
    public void setEntityPreAndPostProcessor(EntityPreAndPostProcessor entityPreAndPostProcessor) {
        this.entityPreAndPostProcessor = entityPreAndPostProcessor;
    }

    @Override
    public Entity putEntity(Entity entity) {
        entity = entityPreAndPostProcessor.prePut(entity);
        entity = entityPreAndPostProcessor.contextWork(entity);
        entity = entitiesStorage.putEntity(entity);
        entity = entityPreAndPostProcessor.postPut(entity);
        return entity;
    }

    @Override
    public Boolean delete(Entity entity) {
        return entitiesStorage.delete(entity);
    }

    @Override
    public Optional<Entity> getByKey(String key, Object id) {
        return entitiesStorage.getByKey(key, id);
    }

    @Override
    public Long countByQuery(String query, String key) {
        return entitiesStorage.countByQuery(queryLanguageProcessor.makeQuery(query), key);
    }

    @Override
    public Optional<Entity> getOneByQuery(String query, String key) {
        Optional<Entity> entity = entitiesStorage.getOneByQuery(queryLanguageProcessor.makeQuery(query), key);
        entity.map(e -> entityPreAndPostProcessor.postPut(e));
        return entity;
    }

    @Override
    public List<Entity> getByQuery(String query, String key) {
        List<Entity> byQuery = entitiesStorage.getByQuery(queryLanguageProcessor.makeQuery(query), key);
        return byQuery.stream().map(entity -> entityPreAndPostProcessor.postPut(entity)).collect(Collectors.toList());
    }

    @Override
    public List<Entity> getByQuery(String query, String key, Optional<Integer> limit) {
        List<Entity> byQuery = entitiesStorage.getByQuery(query, key, limit);
        return byQuery.stream().map(entity -> entityPreAndPostProcessor.postPut(entity)).collect(Collectors.toList());
    }

    @Override
    public List<Entity> getAllByKey(String key) {
        List<Entity> allByKey = entitiesStorage.getAllByKey(key);
        return allByKey.stream().map(entity -> entityPreAndPostProcessor.postPut(entity)).collect(Collectors.toList());
    }
}
