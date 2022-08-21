package com.hotels.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super("EmailAlreadyExistsException: " + message);
    }
}
