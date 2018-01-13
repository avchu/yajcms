package org.yajcms.db.utils.exceptions;

public class NullQueryException extends RuntimeException {
    public NullQueryException() {
        super("Query cannot be null or empty");
    }
}
