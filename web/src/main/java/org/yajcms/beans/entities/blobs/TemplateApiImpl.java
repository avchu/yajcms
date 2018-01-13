package org.yajcms.beans.entities.blobs;

import lombok.extern.slf4j.Slf4j;
import org.yajcms.beans.entities.Entity;
import org.yajcms.db.entities.BlobEntity;

import java.util.List;
import java.util.Optional;

@Slf4j
public class TemplateApiImpl extends FileApiImpl {

    public List<Entity> getAllTemplates() {
        return entitiesDao.getAllByKey(getKeyName());
    }

    @Override
    public Entity put(Entity entity, byte[] source) throws Exception {
        Optional<Entity> fileEntity = getEntity(entity.getPropertyString("filePath"));
        if (fileEntity.isPresent()) {
            BlobEntity blobEntity = blobStorageApi.get(fileEntity.get().getPropertyLong("blobId"));
            blobStorageApi.update(blobEntity, source);
            entity = fileEntity.get();
        } else {
            BlobEntity blobFile = blobStorageApi.put(entity.getPropertyString("filePath"), source);
            entity.putProperty("blobId", blobFile.getOid());
        }
        entitiesDao.putEntity(entity);
        return entity;
    }

    @Override
    public Optional<BlobEntity> get(String filePath) {
        log.debug("Trying get by filePath {}", filePath);
        Optional<Entity> oneByQuery = getEntity(filePath);
        if (!oneByQuery.isPresent()) {
            return Optional.empty();
        }
        BlobEntity source = blobStorageApi.get(oneByQuery.get().getPropertyLong("blobId"));
        return Optional.of(source);
    }

    private Optional<Entity> getEntity(String filePath) {
        Optional<Entity> oneByQuery = entitiesDao.getOneByQuery(String.format("filePath:%s", filePath), getKeyName());
        if (!oneByQuery.isPresent()) {
            log.debug("Entity not found");
        }
        return oneByQuery;

    }
}
