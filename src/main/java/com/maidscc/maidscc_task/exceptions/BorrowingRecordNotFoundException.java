package com.maidscc.maidscc_task.exceptions;

public class BorrowingRecordNotFoundException extends RuntimeException {

    public BorrowingRecordNotFoundException(String message) {
        super(message);
    }

    public BorrowingRecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
