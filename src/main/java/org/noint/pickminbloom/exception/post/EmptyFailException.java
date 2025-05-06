package org.noint.pickminbloom.exception.post;

import org.noint.pickminbloom.exception.PickminBloomException;

public class EmptyFailException extends PickminBloomException {
    public EmptyFailException() {
        super(500, "", "Fail Empty");
    }
}
