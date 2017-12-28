package org.yajcms.beans;

import java.util.Date;

public class SimpleIdGenerator implements EntityIdGenerator {
    @Override
    public Object renderId(String key) {
        return new Date().getTime();
    }
}
