package org.yajcms.beans.entities.nosql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.yajcms.beans.generators.EntityIdGenerator;
import org.yajcms.beans.entities.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntitiesStorageMongoDBImpl implements EntitiesStorage {

    Integer defaultLimit;

    @Value("${org.yajcms.entities.query.limit:100}")
    public void setDefaultLimit(Integer defaultLimit) {
        this.defaultLimit = defaultLimit;
    }

    protected MongoOperations mongoOperations;

    protected EntityIdGenerator entityIdGenerator;

    @Autowired
    public void setEntityIdGenerator(EntityIdGenerator entityIdGenerator) {
        this.entityIdGenerator = entityIdGenerator;
    }

    @Autowired
    public void setMongoOperations(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public Entity putEntity(Entity entity) {
        entity.setId(Optional.of(entity.getId().orElse(entityIdGenerator.renderId(entity.getKey()))));
        checkNotNull(entity.getName());
        checkNotNull(entity.getKey());
        checkNotNull(entity.getId());
        mongoOperations.save(entity, tableName(entity));
        return entity;
    }

    @Override
    public String tableName(Entity entity) {
        return entity.getDomain().orElse("").replace(".", "_") + "_" + entity.getKey();
    }

    public String tableName(Optional<String> domain, String key) {
        return domain.orElse("") + "_" + key;
    }

    @Override
    public Boolean delete(Entity entity) {
        return mongoOperations.remove(entity, tableName(entity)).getDeletedCount() != 0L;
    }

    @Override
    public Optional<Entity> getByKey(String key, Object id) {
        return Optional.ofNullable(mongoOperations.findOne(
                new Query(Criteria.where("id").is(id)), Entity.class, tableName(Optional.empty(), key))
        );
    }

    @Override
    public Long countByQuery(Object query, String key) {
        return mongoOperations.count((Query) query, Entity.class, tableName(Optional.empty(), key));
    }

    @Override
    public Optional<Entity> getOneByQuery(Object query, String key) {
        return Optional.ofNullable(
                mongoOperations.findOne((Query) query, Entity.class, tableName(Optional.empty(), key))
        );
    }

    @Override
    public List<Entity> getByQuery(Object query, String key) {
        return getByQuery(query, key, Optional.empty());
    }

    /**
     * @param query
     * @param key
     * @param limit
     * @return empty non-null list
     */
    @Override
    public List<Entity> getByQuery(Object query, String key, Optional<Integer> limit) {
        Query q = ((Query) query).limit(limit.orElse(defaultLimit));
        return mongoOperations.find(q, Entity.class, tableName(Optional.empty(), key));
    }

    @Override
    public List<Entity> getAllByKey(String key) {
        return mongoOperations.findAll(Entity.class, tableName(Optional.empty(), key));
    }
}
