package org.yajcms.controller.core;

public interface BlobStorageApi {
    BlobEntity get(String path) throws Exception;

    BlobEntity put(String path, byte[] source) throws Exception;
}
