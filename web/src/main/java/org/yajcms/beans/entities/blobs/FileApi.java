package org.yajcms.beans.entities.blobs;

import org.yajcms.beans.entities.Entity;
import org.yajcms.db.entities.BlobEntity;

import java.util.Optional;

public interface FileApi {
    Boolean delete(Entity entity);

    Entity put(Entity entity, byte[] source) throws Exception;

    Optional<BlobEntity> get(String publicUrl);

    Optional<BlobEntity> get(Entity entity);

}
