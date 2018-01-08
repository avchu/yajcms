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
import org.yajcms.beans.entities.Entity;
import org.yajcms.beans.entities.blobs.BlobStorageApi;
import org.yajcms.beans.entities.blobs.FileApi;
import org.yajcms.beans.entities.nosql.EntitiesInitializer;
import org.yajcms.db.entities.BlobEntity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class BlobApiIT {

    @Autowired
    private BlobStorageApi blobStorageApi;

    @Value("classpath:0603c92a523346d3b3f9febd2f46f520.png")
    private Resource res;

    @Autowired
    private FileApi fileApi;

    @Autowired
    private EntitiesInitializer entitiesInitializer;

    @Test
    public void testFileApi() {
        boolean present = false;

        Entity entity = entitiesInitializer.createEntity("File");
        entity.putProperty("publicUrl", "0603c92a523346d3b3f9febd2f46f520.png");
        try {
            present = fileApi.put(entity, ByteStreams.toByteArray(res.getInputStream())).getId().isPresent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(present);
        assertTrue(fileApi.get("0603c92a523346d3b3f9febd2f46f520.png").isPresent());
    }

    @Test
    public void testFileDelete() {
        Entity entity = entitiesInitializer.createEntity("File");
        entity.putProperty("publicUrl", "0603c92a523346d3b3f9febd2f46f520.png");
        try {
            fileApi.put(entity, ByteStreams.toByteArray(res.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(fileApi.delete(entity));
    }

    @Test(expected = RuntimeException.class)
    public void noSuchEntity() {
        entitiesInitializer.createEntity("File2");
    }

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

    @Test(expected = RuntimeException.class)
    public void checkNotFundExceptionByOID() {
        blobStorageApi.get(1L);
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

    @Test
    public void deleteFalseByID() {
        assertFalse(blobStorageApi.delete(1L));
    }

    @Test
    public void deleteTrueByID() throws Exception {
        BlobEntity put = blobStorageApi.put(res.getFilename(), ByteStreams.toByteArray(res.getInputStream()));
        assertTrue(blobStorageApi.delete(put.getOid()));
    }

}
