package org.yajcms.beans;

import org.yajcms.core.Entity;

public interface EntityPreAndPostProcessor {

    Entity prePut(Entity entity);

    Entity postPut(Entity entity);

    Entity contextWork(Entity entity);
}
