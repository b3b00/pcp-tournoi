package org.pcp.tournament.web.exception;

public enum PCPError {
    
    UNAUTHORIZED(1),
    INTERNAL_ERROR(2),
    BAD_REQUEST(3),
    NOT_FOUND(4),
    BAD_FILE(5);

    private final int value;

    private PCPError(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}