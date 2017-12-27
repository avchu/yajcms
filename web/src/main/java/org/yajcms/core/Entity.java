package org.yajcms.core;

import lombok.Data;

import java.util.Optional;

@Data
public class Entity extends EntitiesBase {

    Boolean cache;
    String name;
    Optional<String> domain = Optional.empty();
    Optional<Long> id = Optional.empty();

    public Entity(String name) {
        this.name = name;
    }
}
