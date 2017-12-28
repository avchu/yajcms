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
    Optional<String> domain = Optional.empty();
    Optional<Object> id = Optional.empty();

    public Entity(String name) {
        this.name = name;
    }

    public Entity(Option<JSONObject> jsonObjects, String keyName) {
        cache = jsonObjects.get().getBoolean("cache");
        name = jsonObjects.get().getJSONObject("entity").getString("name");
        key = keyName;
    }
}
