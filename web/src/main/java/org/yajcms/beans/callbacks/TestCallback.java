package org.yajcms.beans.callbacks;

import org.springframework.stereotype.Component;
import org.yajcms.core.Entity;

@Component
@YajCMSCallback("Test")
public class TestCallback {

    @YajCMSPrePut
    public Entity simplePrePutAction(Entity entity) {
        entity.incrementValue("version");
        return entity;
    }

    @YajCMSPostPut
    public Entity simplePostPutAction(Entity entity) {
        entity.incrementValue("version");
        return entity;
    }

    @YajCMSContextWork
    public Entity simpleContextAction(Entity entity) {
        entity.incrementValue("version");
        return entity;
    }
}
