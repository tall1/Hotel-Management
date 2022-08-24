package com.hotels.exceptions;

public class NoAssignmentsForTaskException extends RuntimeException{
    public NoAssignmentsForTaskException(String message) {
        super(message);
    }
}
