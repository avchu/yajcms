package org.yajcms.beans.cache;

import org.yajcms.beans.entities.Entity;

public interface GlobalLock {
    Boolean lock(Entity entity);

    Boolean lock(String key);

    Boolean unlock(Entity entity);

    Boolean unlock(String key);
}
