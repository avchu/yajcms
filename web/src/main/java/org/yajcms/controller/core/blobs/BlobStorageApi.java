package org.yajcms.controller.core.blobs;

import org.yajcms.db.entities.BlobEntity;

public interface BlobStorageApi {
    BlobEntity get(String path) throws Exception;

    Boolean delete(String path);

    BlobEntity put(String path, byte[] source) throws Exception;
}
