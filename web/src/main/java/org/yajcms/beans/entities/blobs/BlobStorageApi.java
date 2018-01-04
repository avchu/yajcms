package org.yajcms.beans.entities.blobs;

import org.yajcms.db.entities.BlobEntity;

public interface BlobStorageApi {
    BlobEntity get(String path);

    BlobEntity get(Long id);

    Boolean delete(String path);

    Boolean delete(long id);

    BlobEntity put(String path, byte[] source) throws Exception;
}
