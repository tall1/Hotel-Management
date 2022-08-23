package com.hotels.exceptions;

public class CannotUpdateTaskNotNewException extends RuntimeException {
    public CannotUpdateTaskNotNewException(String message) {
        super("CannotUpdateTaskNotNewException: " + message);
    }
}
