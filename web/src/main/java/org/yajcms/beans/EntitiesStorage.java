package org.yajcms.beans;

import org.yajcms.core.Entity;

import java.util.List;
import java.util.Optional;

public interface EntitiesStorage {
    Entity storeEntity(Entity entity);

    String tableName(Entity tableName);

    List<Entity> getAllByKey(String key);

    void delete(Entity entity);

    Optional<Entity> getByKey(String key, Object id);
}
