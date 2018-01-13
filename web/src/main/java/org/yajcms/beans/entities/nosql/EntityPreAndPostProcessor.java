package org.yajcms.beans.entities.nosql;

import org.yajcms.beans.entities.Entity;

public interface EntityPreAndPostProcessor {

    Entity prePut(Entity entity);

    Entity postPut(Entity entity);

    Entity contextWork(Entity entity);
}
