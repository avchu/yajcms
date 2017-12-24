package org.yajcms.unit;

import com.google.common.testing.FakeTicker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.yajcms.controller.core.Entity;
import org.yajcms.controller.core.EntityCache;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
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


        when(mongoOperations.findAll(Entity.class, "All"))
                .thenReturn(Collections.singletonList(Entity.builder().hash("1").build()))
                .thenReturn(Collections.singletonList(Entity.builder().hash("2").build()));

        assertEquals(1, entityCache.getAll("All").size());
        fakeTicker.advance(65, TimeUnit.SECONDS);
        assertEquals("2", entityCache.getAll("All").get(0).getHash());

    }

}
