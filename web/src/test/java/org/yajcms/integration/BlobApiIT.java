package org.yajcms.integration;

import com.google.common.io.ByteStreams;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.yajcms.controller.core.blobs.PostgresBlobStorageApi;
import org.yajcms.db.entities.BlobEntity;

import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class BlobApiIT {

    @Autowired
    PostgresBlobStorageApi postgresBlobStorageApi;

    @Value("classpath:0603c92a523346d3b3f9febd2f46f520.png")
    Resource res;

    @Test
    public void checkCache() {
        try {
            BlobEntity put = postgresBlobStorageApi.put(res.getFilename(), ByteStreams.toByteArray(res.getInputStream()));
            assertNotEquals(put.getOid(), null);
            BlobEntity blobEntity = postgresBlobStorageApi.get(res.getFilename());
            assertNotEquals(0, blobEntity.getSource().length);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
