package com.hotels.entities.enums;

public enum RequestImportance {
    NOT_IMPORTANT,
    NICE_TO_HAVE,
    MUST;
    public static RequestImportance getRequestImportanceByInt(Integer importance){
        switch (importance){
            case 3:
                return MUST;
            case 2:
                return NICE_TO_HAVE;
            default:
                return NOT_IMPORTANT;
        }
    }

}