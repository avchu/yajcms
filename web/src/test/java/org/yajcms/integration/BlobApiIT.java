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
import org.yajcms.core.blobs.BlobStorageApi;
import org.yajcms.core.blobs.PostgresBlobStorageApi;
import org.yajcms.db.entities.BlobEntity;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class BlobApiIT {

    @Autowired
    BlobStorageApi blobStorageApi;

    @Value("classpath:0603c92a523346d3b3f9febd2f46f520.png")
    Resource res;

    @Test
    public void checkCache() {

        BlobEntity put = null;
        try {
            put = blobStorageApi.put(res.getFilename(), ByteStreams.toByteArray(res.getInputStream()));
            assertFalse(put.getContentHash() == null);
            assertFalse(put.getPath() == null);
            assertFalse(put.getHash() == null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotEquals(put.getOid(), null);
        BlobEntity blobEntity = blobStorageApi.get(res.getFilename());
        assertNotEquals(0, blobEntity.getSource().length);

        blobStorageApi.delete(res.getFilename());
    }

    @Test(expected = RuntimeException.class)
    public void checkNotFundException() {
        blobStorageApi.get("lolololololol");
    }

    @Test
    public void deleteFalse() {
        assertFalse(blobStorageApi.delete("ololololo"));
    }

    @Test
    public void deleteTrue() throws Exception {
        blobStorageApi.put(res.getFilename(), ByteStreams.toByteArray(res.getInputStream()));
        assertTrue(blobStorageApi.delete(res.getFilename()));
    }

}
