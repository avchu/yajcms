package org.yajcms.core.blobs;

import org.yajcms.db.entities.BlobEntity;

public interface BlobStorageApi {
    BlobEntity get(String path);

    Boolean delete(String path);

    BlobEntity put(String path, byte[] source) throws Exception;
}
