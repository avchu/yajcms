package org.yajcms.db.utils.exceptions;

public class YajCMSFieldNotFoundException extends RuntimeException {
    public YajCMSFieldNotFoundException(String key, String entityKey) {
        super(String.format("No such field in entity: %s -> %s", key, entityKey));
    }
}
