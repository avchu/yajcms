package org.yajcms.beans.entities.nosql;

import io.vavr.API;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.yajcms.beans.entities.Entity;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Component
@Slf4j
public class EntitiesInitializer {

    @Setter
    private List<Resource> resources;
    @Setter
    private HashMap<String, JSONObject> entitiesProto;
    @Setter
    @Getter
    private HashMap<String, Entity> entities;

    @PostConstruct
    public void init() throws IOException {
        setEntitiesProto(HashMap.empty());
        setEntities(HashMap.empty());
        this.setResources(List.empty());

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        resources = List.of(resolver.getResources("/entities/**/*.json"));
        List.ofAll(resources)
                .map(API.unchecked(Resource::getFilename)).forEach(f -> log.debug("{}", f));
        resources.forEach(x -> {
            try {
                initEntities(x.getFilename(), x.getInputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        entitiesProto.keySet().forEach(key -> entities = entities.put(key, new Entity(entitiesProto.get(key), key)));
    }

    private void initEntities(String filename, InputStream json) throws IOException {
        entitiesProto = entitiesProto.put(filename.replace(".json", ""),
                new JSONObject(IOUtils.toString(json, Charset.forName("utf-8"))));
    }

    public Entity createEntity(String key) {
        if(!entitiesProto.containsKey(key)) {
            throw new RuntimeException("No such entity in classpath");
        }
        return new Entity(entitiesProto.get(key), key);
    }
}
