package org.yajcms.beans.pipeline;

import org.yajcms.core.Entity;

import java.util.List;
import java.util.Optional;

public interface EntitiesDao {
    Entity storeEntity(Entity entity);

    List<Entity> getAllByKey(String key);

    void delete(Entity entity);

    Optional<Entity> getByKey(String key, Object id);
}
