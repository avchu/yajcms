package org.yajcms.integration;

import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.yajcms.beans.generators.EntityIdGenerator;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class SimpleIDGeneratorIT {

    @Autowired
    EntityIdGenerator entityIdGenerator;

    @Test
    public void testIdGenerator() {
        assertTrue(entityIdGenerator.renderId("File") instanceof Long);
    }
}
