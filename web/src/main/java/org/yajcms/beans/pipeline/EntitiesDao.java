package org.yajcms.beans.pipeline;

import org.yajcms.beans.entities.Entity;

import java.util.List;
import java.util.Optional;

public interface EntitiesDao {
    Entity putEntity(Entity entity);

    Boolean delete(Entity entity);

    Optional<Entity> getByKey(String key, Object id);

    Long countByQuery(String query, String key);

    Optional<Entity> getOneByQuery(String query, String key);

    List<Entity> getByQuery(String query, String key);

    List<Entity> getByQuery(String query, String key, Optional<Integer> limit);

    List<Entity> getAllByKey(String key);

    List<Long> getReferenceIds(String key, Entity entity);

    List<Entity> getReferenceEntities(String key, Entity entity);
}
