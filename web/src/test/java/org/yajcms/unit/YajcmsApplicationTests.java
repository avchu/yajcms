package org.yajcms.unit;

import com.github.avchu.json.JSONObject;
import com.google.common.testing.FakeTicker;
import io.vavr.collection.List;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.yajcms.beans.entities.nosql.EntitiesInitializer;
import org.yajcms.beans.cache.EntityCache;
import org.yajcms.beans.pipeline.EntitiesDao;
import org.yajcms.beans.entities.Entity;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class YajcmsApplicationTests {

    private EntityCache entityCache = new EntityCache();

    @Mock
    private EntitiesDao entitiesDao;

    @Mock
    private EntitiesInitializer entitiesInitializer;

    private Entity entity;

    @Before
    public void setUp() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String testJson = IOUtils.toString(classLoader.getResource("Test.json").openStream(), Charset.forName("utf-8"));
        entity = new Entity(Optional.of(new JSONObject(testJson)), "Test");
    }

    @Test
    public void checkCache() {
        FakeTicker fakeTicker = new FakeTicker();
        entityCache.setEntitiesDao(entitiesDao);
        entityCache.setEntitiesInitializer(entitiesInitializer);
        entityCache.setCacheExpirationInSeconds(60);
        entityCache.setTicker(fakeTicker);
        entityCache.initCache();


        Entity entity1 = entity.toBuilder().build();
        entity1.setProperties(new HashMap<>(entity.getProperties()));

        entity1.putProperty("hash", Long.valueOf(1L));
        entity1.setCache(true);

        Entity entity2 = entity.toBuilder().build();
        entity2.setProperties(new HashMap<>(entity.getProperties()));

        entity2.putProperty("hash", Long.valueOf(2L));
        entity2.setCache(true);


        when(entitiesDao.getAllByKey(any()))
                .thenReturn(Arrays.asList(entity1))
                .thenReturn(Arrays.asList(entity2));

        assertEquals(Long.valueOf(1L), entityCache.getAll("Test").get(0).getPropertyLong("hash"));
        fakeTicker.advance(65, TimeUnit.SECONDS);
        assertEquals(Long.valueOf(2L), entityCache.getAll("Test").get(0).getPropertyLong("hash"));
    }

    @Test
    public void coverallTest() {
        Entity e = new Entity();
        assertNotEquals(e, null);
    }

    @Test
    public void testGetBoolean() {
        entity.putProperty("boolean", false);
        assertFalse(entity.getPropertyBoolean("boolean"));
    }

    @Test
    public void testGetBooleanNoValue() {
        assertFalse(entity.getPropertyBoolean("boolean"));
    }

    @Test
    public void testGetBooleanWithOptional() {
        assertTrue(entity.getPropertyBoolean("boolean", Optional.of(true)));
    }

    @Test
    public void testGetLong() {
        entity.putProperty("long", 1L);
        assertEquals(Long.valueOf(1L), entity.getPropertyLong("long"));
    }

    @Test
    public void testGetLongNoValue() {
        assertEquals(Long.valueOf(Long.MIN_VALUE), entity.getPropertyLong("long"));
    }

    @Test
    public void testGetLongWithOptional() {
        assertEquals(Long.valueOf(100L), entity.getPropertyLong("long", Optional.of(100L)));
    }


    @Test
    public void testGetString() {
        entity.putProperty("string", "lolo");
        assertEquals("lolo", entity.getPropertyString("string"));
    }

    @Test
    public void testGetStringNoValue() {
        assertTrue(entity.getPropertyString("string").isEmpty());
    }

    @Test
    public void testGetStringWithOptional() {
        assertTrue(entity.getPropertyString("string", Optional.of("lolo")).equals("lolo"));
    }

    @Test
    public void testGetList() {
        entity.putProperty("list", List.of(1L));
        assertEquals(1L, entity.getPropertyList("list").last().longValue());
    }

    @Test
    public void testGetListNoValue() {
        assertTrue(entity.getPropertyList("list").isEmpty());
    }

    @Test
    public void testGetListWithOptional() {
        assertEquals(Long.valueOf(3L), entity.getPropertyList("list", Optional.of(List.of(3L))).last());
    }

}
