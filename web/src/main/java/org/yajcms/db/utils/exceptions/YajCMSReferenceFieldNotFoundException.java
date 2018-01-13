package org.yajcms.db.utils.exceptions;

public class YajCMSReferenceFieldNotFoundException extends RuntimeException {
    public YajCMSReferenceFieldNotFoundException(String key, String entityKey) {
        super(String.format("No reference field: %s -> %s", key, entityKey));
    }
}