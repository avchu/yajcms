package org.yajcms.integration;

import io.vavr.collection.List;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.yajcms.beans.EntitiesInitializer;
import org.yajcms.beans.EntitiesStorage;
import org.yajcms.core.Entity;

import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class EntityIT {

    @Autowired
    EntitiesInitializer entitiesInitializer;

    @Autowired
    EntitiesStorage entitiesStorage;

    @Test
    public void storeEntity() {
        Entity toPut = new Entity("File");
        toPut.putProperty("str", "lo");
        toPut.putProperty("long", 2L);
        toPut.putProperty("boolean", true);
        toPut.putProperty("list", List.of(3L, 4L));

        Entity e = entitiesStorage.storeEntity(toPut);
        assertTrue(e.getId().isPresent());
    }
}
