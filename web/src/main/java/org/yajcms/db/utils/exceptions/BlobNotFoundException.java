package org.yajcms.db.utils.exceptions;

public class BlobNotFoundException extends RuntimeException {
    public BlobNotFoundException(String key) {
        super(String.format("No such blob: %s", key));
    }
    public BlobNotFoundException(Long key) {
        super(String.format("No such blob: %s", key.toString()));
    }
}
