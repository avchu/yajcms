package org.yajcms.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import org.yajcms.core.Entity;

import java.util.Date;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
public class EntitiesStorage {


    MongoOperations mongoOperations;

    @Autowired
    public void setMongoOperations(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public Entity storeEntity(Entity entity) {
        entity.setId(Optional.of(entity.getId().orElse(new Date().getTime())));
        checkNotNull(entity.getName());
        mongoOperations.save(entity,
                entity.getDomain().orElse("").replace(".", "_") + "_" + entity.getName());
        return entity;
    }

}
