package org.yajcms.unit;

import com.google.common.testing.FakeTicker;
import io.vavr.collection.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.yajcms.core.Entity;
import org.yajcms.beans.EntityCache;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class YajcmsApplicationTests {

    EntityCache entityCache = new EntityCache();
    @Mock
    MongoOperations mongoOperations;

    @Test
    public void contextLoads() {
    }

    @Test
    public void checkCache() {
        FakeTicker fakeTicker = new FakeTicker();
        entityCache.setMongoOperations(mongoOperations);
        entityCache.setCacheExpirationInSeconds(60);
        entityCache.setTicker(fakeTicker);
        entityCache.initCache();


        Entity entity1 = new Entity("Test");
        entity1.putProperty("hash", Long.valueOf(1L));
        Entity entity2 = new Entity("Test");
        entity2.putProperty("hash", Long.valueOf(2L));

        when(mongoOperations.findAll(Entity.class, "All"))
                .thenReturn(Arrays.asList(entity1))
                .thenReturn(Arrays.asList(entity2));

        assertEquals(Long.valueOf(1L), entityCache.getAll("All").get(0).getPropertyLong("hash"));
        fakeTicker.advance(65, TimeUnit.SECONDS);
        assertEquals(Long.valueOf(2L), entityCache.getAll("All").get(0).getPropertyLong("hash"));
    }

    @Test
    public void testGetBoolean() {
        Entity entity = new Entity("Test");
        entity.putProperty("boolean", false);
        assertFalse(entity.getPropertyBoolean("boolean"));
    }

    @Test
    public void testGetBooleanNoValue() {
        Entity entity = new Entity("Test");
        assertFalse(entity.getPropertyBoolean("boolean"));
    }

    @Test
    public void testGetBooleanWithOptional() {
        Entity entity = new Entity("Test");
        assertTrue(entity.getPropertyBoolean("boolean", Optional.of(true)));
    }

    @Test
    public void testGetLong() {
        Entity entity = new Entity("Test");
        entity.putProperty("long", 1L);
        assertEquals(Long.valueOf(1L), entity.getPropertyLong("long"));
    }

    @Test
    public void testGetLongNoValue() {
        Entity entity = new Entity("Test");
        assertEquals(Long.valueOf(Long.MIN_VALUE), entity.getPropertyLong("long"));
    }

    @Test
    public void testGetLongWithOptional() {
        Entity entity = new Entity("Test");
        assertEquals(Long.valueOf(100L), entity.getPropertyLong("long", Optional.of(100L)));
    }


    @Test
    public void testGetString() {
        Entity entity = new Entity("Test");
        entity.putProperty("string", "lolo");
        assertEquals("lolo", entity.getPropertyString("string"));
    }

    @Test
    public void testGetStringNoValue() {
        Entity entity = new Entity("Test");
        assertTrue(entity.getPropertyString("string").isEmpty());
    }

    @Test
    public void testGetStringWithOptional() {
        Entity entity = new Entity("Test");
        assertTrue(entity.getPropertyString("string", Optional.of("lolo")).equals("lolo"));
    }

    @Test
    public void testGetList() {
        Entity entity = new Entity("Test");
        entity.putProperty("list", List.of(1L));
        assertEquals(1L, entity.getPropertyList("list").last());
    }

    @Test
    public void testGetListNoValue() {
        Entity entity = new Entity("Test");
        assertTrue(entity.getPropertyList("List").isEmpty());
    }

    @Test
    public void testGetListWithOptional() {
        Entity entity = new Entity("Test");
        assertEquals(Long.valueOf(3L), entity.getPropertyList("list", Optional.of(List.of(3L))).last());
    }

}
