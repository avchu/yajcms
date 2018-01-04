package org.yajcms.beans.entities.nosql;

import org.yajcms.beans.entities.Entity;

import java.util.List;
import java.util.Optional;

public interface EntitiesStorage {
    Entity putEntity(Entity entity);

    String tableName(Entity tableName);

    Boolean delete(Entity entity);

    Optional<Entity> getByKey(String key, Object id);

    Long countByQuery(Object query, String key);

    Optional<Entity> getOneByQuery(Object query, String key);

    List<Entity> getByQuery(Object query, String key);

    List<Entity> getByQuery(Object query, String key, Optional<Integer> limit);
}
