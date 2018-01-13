package org.yajcms.integration;

import com.github.avchu.json.JSONObject;
import io.vavr.collection.List;
import lombok.extern.java.Log;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.yajcms.beans.cache.EntityCache;
import org.yajcms.beans.entities.Entity;
import org.yajcms.beans.entities.nosql.EntitiesInitializer;
import org.yajcms.beans.pipeline.EntitiesDao;
import org.yajcms.db.utils.exceptions.YajCMSFieldNotFoundException;
import org.yajcms.db.utils.exceptions.YajCMSReferenceFieldNotFoundException;

import java.nio.charset.Charset;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class EntityIT {

    @Autowired
    EntitiesInitializer entitiesInitializer;

    @Autowired
    EntitiesDao entitiesDao;

    @Autowired
    EntityCache entityCache;

    private Entity toPut;
    private Entity toPut2;
    private Entity toPut3;

    @Before
    public void setUp() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String testJson = IOUtils.toString(classLoader.getResource("Test.json").openStream(), Charset.forName("utf-8"));
        String testJsonW = IOUtils.toString(classLoader.getResource("TestW.json").openStream(), Charset.forName("utf-8"));
        toPut = new Entity(Optional.of(new JSONObject(testJson)), "Test");
        toPut.putProperty("string", "lo");
        toPut.putProperty("long", 2L);
        toPut.putProperty("boolean", true);
        toPut.putProperty("list", List.of(3L, 4L));

        toPut2 = new Entity(Optional.of(new JSONObject(testJson)), "Test");
        toPut2.putProperty("string", "lo");
        toPut2.putProperty("long", 2L);
        toPut2.putProperty("boolean", true);
        toPut2.putProperty("list", List.of(3L, 4L));

        toPut3 = new Entity(Optional.of(new JSONObject(testJsonW)), "TestW");
        toPut3.putProperty("string", "lo");
        toPut3.putProperty("long", 2L);
        toPut3.putProperty("boolean", true);
        toPut3.putProperty("list", List.of(3L, 4L));
    }

    @Test
    public void checkPrePutPostPutAnnotation() {
        Entity e = entitiesDao.putEntity(toPut);
        assertEquals(e.getPropertyLong("version"), Long.valueOf(3));
    }

    @Test
    public void checkNullInKey() {
        Entity e = new Entity("Test");
        assertTrue(e.getKey().equals("Test"));
    }

    @Test
    public void checkCacheFalse() {
        Entity e = new Entity("Test");
        assertFalse(e.getCache());
    }

    @Test
    public void storeEntity() {
        Entity e = entitiesDao.putEntity(toPut);
        assertTrue(e.getId().isPresent());
    }

    @Test
    public void testDelete() {
        Entity e = entitiesDao.putEntity(toPut);
        assertTrue(e.getId().isPresent());
        entitiesDao.delete(e);

        assertEquals(entitiesDao.getByKey("File", e.getId()).isPresent(), false);
    }

    @Test
    public void countByQuery() {
        entitiesDao.putEntity(toPut);
        assertTrue(entitiesDao.countByQuery("string:lo", "Test") != 0L);
    }

    @Test
    public void getOneByQuery() {
        entitiesDao.putEntity(toPut);
        assertNotEquals(entitiesDao.getOneByQuery("string:lo", "Test"), null);
    }

    @Test
    public void getByQuery() {
        entitiesDao.putEntity(toPut);
        assertNotEquals(entitiesDao.getByQuery("string:lo", "Test").size(), 0);
    }

    @Test
    public void checkReferenceId() {
        entitiesDao.putEntity(toPut2);
        toPut.putProperty("blocks", List.of(toPut2.getId().get()));
        entitiesDao.putEntity(toPut);
        assertNotEquals(entitiesDao.getReferenceIds("blocks", toPut).size(), 0);
    }

    @Test(expected = YajCMSFieldNotFoundException.class)
    public void checkReferenceIdEx() {
        entitiesDao.putEntity(toPut2);
        toPut.putProperty("blockss", List.of(toPut2.getId().get()));
    }

    @Test
    public void checkReferenceEntities() {
        entitiesDao.putEntity(toPut2);
        toPut.putProperty("blocks", List.of(toPut2.getId().get()));
        entitiesDao.putEntity(toPut);
        assertTrue(entitiesDao.getReferenceEntities("blocks", toPut).get(0).getId().equals(toPut2.getId()));
    }

    @Test(expected = YajCMSReferenceFieldNotFoundException.class)
    public void checkReferenceEntitiesEx() {
        entitiesDao.putEntity(toPut2);
        toPut3.putProperty("blocks", List.of(1L));
        entitiesDao.putEntity(toPut3);
        assertTrue(entitiesDao.getReferenceEntities("blocks", toPut3).get(0).getId().equals(toPut2.getId()));
    }
}
