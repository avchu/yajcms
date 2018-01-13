package org.yajcms.beans.entities;

import com.github.avchu.json.JSONArray;
import com.github.avchu.json.JSONObject;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.yajcms.core.YajCMSFiled;

import java.util.Optional;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class Entity extends EntitiesBase {

    private Boolean cache;
    private String name;
    private String key;
    private Optional<String> domain;
    private Optional<Object> id;


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
        cache = jsonObjects.orElse(new JSONObject()).optBoolean("cache");

        name = jsonObjects.orElse(
                new JSONObject().put("entity", new JSONObject().put("name", keyName))
        ).getJSONObject("entity").getString("name");

        key = keyName;

        JSONArray jsonArray = jsonObjects.orElse(new JSONObject()
                .put("entity", new JSONObject().put("fields", new JSONArray()))
        ).getJSONObject("entity").getJSONArray("fields");

        for (int i = 0; i < jsonArray.length(); i++) {
            properties.put(jsonArray.getJSONObject(i).getString("id"),
                    YajCMSFiled.builder()
                            .id(jsonArray.getJSONObject(i).getString("id"))
                            .name(jsonArray.getJSONObject(i).getString("name"))
                            .type(jsonArray.getJSONObject(i).getString("type"))
                            .ref(jsonArray.getJSONObject(i).optString("ref").orElse(""))
                            .build()
            );
        }
    }

    public Entity(String name) {
        this(Optional.empty(), name);
    }

    public Entity() {
    }

    public void incrementValue(String key) {
        putProperty(key, this.getPropertyLong(key, Optional.of(0L)) + 1L);
    }


}
