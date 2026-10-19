package org.lunev.todo.exception;

public class DaoException extends RuntimeException {
    public DaoException(Throwable message) {
        super(message);
    }
}
