package org.yajcms.beans;

import io.vavr.API;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Component
@Slf4j
public class EntitiesInitializer {

    List<Resource> resources = List.empty();
    HashMap<String, JSONObject> entitiesProto = HashMap.empty();

    @PostConstruct
    public void init() throws IOException {
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
        entitiesProto.keySet().forEach(key -> {});
    }

    public void initEntities(String filename, InputStream json) {
        try {
            entitiesProto.put(filename.replace(".json", ""),
                    new JSONObject(IOUtils.toString(json, Charset.forName("utf-8"))));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
