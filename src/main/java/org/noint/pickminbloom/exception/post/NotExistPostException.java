package org.noint.pickminbloom.exception.post;

import org.noint.pickminbloom.exception.PickminBloomException;

public class NotExistPostException extends PickminBloomException {
    public NotExistPostException(String field) {
        super(404, field, "존재 하지 않는 엽서");
    }
}
