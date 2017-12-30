package org.yajcms.integration;

import io.vavr.collection.List;
import lombok.extern.java.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.yajcms.beans.EntitiesInitializer;
import org.yajcms.beans.EntitiesStorage;
import org.yajcms.beans.EntityCache;
import org.yajcms.core.Entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class EntityIT {

    @Autowired
    EntitiesInitializer entitiesInitializer;

    @Autowired
    EntitiesStorage entitiesStorage;

    @Autowired
    EntityCache entityCache;

    Entity toPut;

    @Before
    public void setUp() {
        toPut = new Entity("File");
        toPut.putProperty("str", "lo");
        toPut.putProperty("long", 2L);
        toPut.putProperty("boolean", true);
        toPut.putProperty("list", List.of(3L, 4L));
    }

    @Test
    public void checkNullInKey() {
        Entity e = new Entity("File");
        assertTrue(e.getKey().equals("File"));
    }

    @Test
    public void checkCacheFalse() {
        Entity e = new Entity("File");
        assertFalse(e.getCache());
    }

    @Test
    public void storeEntity() {
        Entity e = entitiesStorage.storeEntity(toPut);
        assertTrue(e.getId().isPresent());
    }

    @Test
    public void testDelete() {
        Entity e = entitiesStorage.storeEntity(toPut);
        assertTrue(e.getId().isPresent());
        entitiesStorage.delete(e);

        assertEquals(entitiesStorage.getByKey("File", e.getId()).isPresent(), false);
    }


}
