package org.yajcms.beans.entities;


import io.vavr.collection.List;
import lombok.Getter;
import lombok.Setter;
import org.yajcms.core.YajCMSFiled;

import java.util.HashMap;
import java.util.Optional;

public abstract class EntitiesBase {
    @Getter
    protected HashMap<String, YajCMSFiled> properties = new HashMap<>();

    public void putProperty(String key, Object property) {
        if (!properties.containsKey(key)) {
            throw new RuntimeException("No such field in entity: " + key);
        }

        properties.put(key, properties.get(key).toBuilder().value(property).build());
    }

    public Boolean getPropertyBoolean(String property, Optional<Boolean> defaultValue) {
        return (Boolean) Optional.ofNullable(properties.get(property).getValue()).orElse(defaultValue.orElse(false));
    }

    public Boolean getPropertyBoolean(String property) {
        return getPropertyBoolean(property, Optional.empty());
    }

    public String getPropertyString(String property, Optional<String> defaultValue) {
        return (String) Optional.ofNullable(properties.get(property).getValue()).orElse(defaultValue.orElse(""));
    }

    public String getPropertyString(String property) {
        return getPropertyString(property, Optional.empty());
    }

    public Long getPropertyLong(String property, Optional<Long> defaultValue) {
        return (Long) Optional.ofNullable(properties.get(property).getValue()).orElse(defaultValue.orElse(Long.MIN_VALUE));
    }

    public Long getPropertyLong(String property) {
        return getPropertyLong(property, Optional.empty());
    }

    public List getPropertyList(String property, Optional<List> defaultValue) {
        return (List) Optional.ofNullable(properties.get(property).getValue()).orElse(defaultValue.orElse(List.empty()));
    }

    public List getPropertyList(String property) {
        return getPropertyList(property, Optional.empty());
    }

}
