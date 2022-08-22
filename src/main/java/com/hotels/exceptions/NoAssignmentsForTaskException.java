package com.hotels.exceptions;

public class NoAssignmentsForTaskException extends RuntimeException{
    public NoAssignmentsForTaskException(String message) {
        super("There are no assignments for task: " + message);
    }
}
