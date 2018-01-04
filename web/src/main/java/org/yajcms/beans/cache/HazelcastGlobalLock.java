package org.yajcms.beans.cache;

import org.yajcms.beans.entities.Entity;

public class HazelcastGlobalLock implements GlobalLock {
    @Override
    public Boolean lock(Entity entity) {
        return null;
    }

    @Override
    public Boolean lock(String key) {
        return null;
    }

    @Override
    public Boolean unlock(Entity entity) {
        return null;
    }

    @Override
    public Boolean unlock(String key) {
        return null;
    }
}
