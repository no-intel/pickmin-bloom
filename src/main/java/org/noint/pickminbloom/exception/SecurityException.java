package org.noint.pickminbloom.exception;

public class SecurityException extends PickminBloomException {
    public SecurityException() {
        super(500, "", "시큐리티 에러 테스트");
    }
}
