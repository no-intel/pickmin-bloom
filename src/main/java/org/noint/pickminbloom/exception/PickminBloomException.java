package org.noint.pickminbloom.exception;

import lombok.Getter;

@Getter
public class PickminBloomException extends RuntimeException {
    private int code;
    private String field;
    private String message;

    public PickminBloomException(int code, String field, String message) {
        this.code = code;
        this.field = field;
        this.message = message;
    }
}