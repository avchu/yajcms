package org.yajcms.beans.entities.blobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.yajcms.beans.entities.Entity;
import org.yajcms.beans.pipeline.EntitiesDao;
import org.yajcms.db.entities.BlobEntity;

import java.util.Optional;

@Slf4j
public class FileApiImpl implements FileApi {

    private String keyName;

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    private String filePathUrl;

    @Value("${org.yajcms.blob.public.path.url:/_file/}")
    public void setFilePathUrl(String filePathUrl) {
        this.filePathUrl = filePathUrl;
    }

    private BlobStorageApi blobStorageApi;

    @Autowired
    public void setBlobStorageApi(BlobStorageApi blobStorageApi) {
        this.blobStorageApi = blobStorageApi;
    }

    private EntitiesDao entitiesDao;

    @Autowired
    public void setEntitiesDao(EntitiesDao entitiesDao) {
        this.entitiesDao = entitiesDao;
    }

    @Override
    public Boolean delete(Entity entity) {
        log.debug("Deleting file: {}", entity);
        Boolean blobDelete = blobStorageApi.delete(entity.getPropertyLong("blobId"));
        Boolean entityDelete = entitiesDao.delete(entity);
        return (blobDelete && entityDelete);
    }

    @Override
    public Entity put(Entity entity, byte[] source) throws Exception {
        BlobEntity blobFile = blobStorageApi.put(entity.getPropertyString("publicUrl"), source);
        entity.putProperty("blobId", blobFile.getOid());
        entitiesDao.putEntity(entity);
        return entity;
    }

    @Override
    public Optional<BlobEntity> get(String publicUrl) {
        log.debug("Trying get by url {}", publicUrl);
        Optional<Entity> oneByQuery = entitiesDao.getOneByQuery(String.format("publicUrl:%s", publicUrl), keyName);
        if (!oneByQuery.isPresent()) {
            log.debug("Entity not found");
            return Optional.empty();
        }
        BlobEntity source = blobStorageApi.get(oneByQuery.get().getPropertyLong("blobId"));
        return Optional.of(source);
    }
}
