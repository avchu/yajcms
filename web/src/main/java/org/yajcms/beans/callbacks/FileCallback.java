package org.yajcms.beans.callbacks;

import org.springframework.stereotype.Component;
import org.yajcms.core.Entity;

@Component
@YajCMSCallback("Test")
public class FileCallback {

    @YajCMSPrePut
    public Entity simplePrePutaction(Entity entity) {
        Entity copy = entity.toBuilder().build();
        copy.incrementValue("version");
        return copy;
    }
}
