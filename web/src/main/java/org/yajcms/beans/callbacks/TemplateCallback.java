package org.yajcms.beans.callbacks;

import org.springframework.stereotype.Component;
import org.yajcms.beans.entities.Entity;

@Component
@YajCMSCallback("Template")
public class TemplateCallback {

    @YajCMSPrePut
    public Entity incrementVersion(Entity entity) {
        entity.incrementValue("version");
        return entity;
    }
}
