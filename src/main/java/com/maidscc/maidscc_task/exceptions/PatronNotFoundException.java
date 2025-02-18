package com.maidscc.maidscc_task.exceptions;

public class PatronNotFoundException extends RuntimeException {

    public PatronNotFoundException(String message) {
        super(message);
    }

    public PatronNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
