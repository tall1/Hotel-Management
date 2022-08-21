package com.hotels.entities.enums;

import com.hotels.exceptions.CannotUpdateTaskNotNewException;

public enum TaskStatus {
    NEW {
        @Override
        public String getStatusName() {
            return "NEW";
        }
    },
    IN_PROGRESS {
        @Override
        public String getStatusName() {
            return "IN_PROGRESS";
        }
    },
    DONE {
        @Override
        public String getStatusName() {
            return "DONE";
        }
    };
    public abstract String getStatusName();
    public static TaskStatus getTaskStatus(String status) throws CannotUpdateTaskNotNewException {
        switch(status.toLowerCase()){
            case "new":
                return NEW;
            case "in_progress":
                return IN_PROGRESS;
            case "done":
                return DONE;
            default:
                throw new CannotUpdateTaskNotNewException("Task status: " + status + " is not valid status.");
        }
    }
}
