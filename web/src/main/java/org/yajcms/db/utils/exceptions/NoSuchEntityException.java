package org.yajcms.db.utils.exceptions;

public class NoSuchEntityException extends RuntimeException {

    public NoSuchEntityException(String key) {
        super(String.format("No such entity in classpath: %s", key));
    }
}
