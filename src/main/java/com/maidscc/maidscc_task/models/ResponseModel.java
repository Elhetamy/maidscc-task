package com.maidscc.maidscc_task.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModel<T> {
    private int status;
    private String message;
    private T data;
    private LocalDateTime timestamp = LocalDateTime.now();

    // Constructor for success responses
    public ResponseModel(T data, String message, int status) {
        this.data = data;
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for failure responses
    public ResponseModel(String message, int status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}
