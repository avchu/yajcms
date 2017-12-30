package org.yajcms.core;

import io.vavr.control.Option;
import lombok.Data;
import org.json.JSONObject;

import java.util.Optional;

@Data
public class Entity extends EntitiesBase {

    Boolean cache;
    String name;
    String key;
    Optional<String> domain;
    Optional<Object> id;

    /**
     * Converter vavr to java8
     *
     * @param jsonObjects
     * @param keyName
     */
    public Entity(Option<JSONObject> jsonObjects, String keyName) {
        this(Optional.of(jsonObjects.getOrElse(new JSONObject())), keyName);
    }

    public Entity(Optional<JSONObject> jsonObjects, String keyName) {
        domain = Optional.empty();
        id = Optional.empty();
        cache = jsonObjects.orElse(new JSONObject().put("cache", false)).getBoolean("cache");
        name = jsonObjects.orElse(
                new JSONObject().put("entity", new JSONObject().put("name", keyName))
        ).getJSONObject("entity").getString("name");
        key = keyName;
    }

    public Entity(String name) {
        this(Optional.empty(), name);
    }

    public Entity() {
    }
}
