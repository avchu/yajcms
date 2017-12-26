package org.yajcms.core;


import io.vavr.collection.List;

import java.util.HashMap;
import java.util.Optional;

public abstract class EntitiesBase {
    HashMap<String, Object> properties = new HashMap();

    public void putProperty(String key, Object property) {
        properties.put(key, property);
    }

    public Boolean getPropertyBoolean(String property, Optional<Boolean> defaultValue) {
        return (Boolean) Optional.ofNullable(properties.get(property)).orElse(defaultValue.orElse(false));
    }

    public Boolean getPropertyBoolean(String property) {
        return getPropertyBoolean(property, Optional.empty());
    }

    public String getPropertyString(String property, Optional<String> defaultValue) {
        return (String) Optional.ofNullable(properties.get(property)).orElse(defaultValue.orElse(""));
    }

    public String getPropertyString(String property) {
        return getPropertyString(property, Optional.empty());
    }

    public Long getPropertyLong(String property, Optional<Long> defaultValue) {
        return (Long) Optional.ofNullable(properties.get(property)).orElse(defaultValue.orElse(Long.MIN_VALUE));
    }

    public Long getPropertyLong(String property) {
        return getPropertyLong(property, Optional.empty());
    }

    public List getPropertyList(String property, Optional<List> defaultValue) {
        return (List) Optional.ofNullable(properties.get(property)).orElse(defaultValue.orElse(List.empty()));
    }

    public List getPropertyList(String property) {
        return getPropertyList(property, Optional.empty());
    }
}
