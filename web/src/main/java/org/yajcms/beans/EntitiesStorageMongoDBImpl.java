package org.yajcms.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.yajcms.beans.generators.EntityIdGenerator;
import org.yajcms.core.Entity;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntitiesStorageMongoDBImpl implements EntitiesStorage {

    private MongoOperations mongoOperations;

    private EntityIdGenerator entityIdGenerator;

    @Autowired
    public void setEntityIdGenerator(EntityIdGenerator entityIdGenerator) {
        this.entityIdGenerator = entityIdGenerator;
    }

    @Autowired
    public void setMongoOperations(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public Entity storeEntity(Entity entity) {
        entity.setId(Optional.of(entity.getId().orElse(entityIdGenerator.renderId(entity.getKey()))));
        checkNotNull(entity.getName());
        checkNotNull(entity.getKey());
        checkNotNull(entity.getId());
        mongoOperations.save(entity, tableName(entity));
        return entity;
    }

    @Override
    public String tableName(Entity entity) {
        return entity.getDomain().orElse("").replace(".", "_") + "_" + entity.getName();
    }

    public String tableName(Optional<String> domain, String key) {
        return domain.orElse("") + "_" + key;
    }

    @Override
    public List<Entity> getAllByKey(String key) {
        return mongoOperations.findAll(Entity.class, tableName(Optional.empty(), key));
    }

    @Override
    public void delete(Entity entity) {
        mongoOperations.remove(entity, tableName(entity));
    }

    @Override
    public Optional<Entity> getByKey(String key, Object id) {
        return Optional.ofNullable(mongoOperations.findOne(
                new Query(Criteria.where("id").is(id)), Entity.class, tableName(Optional.empty(), key))
        );
    }
}
