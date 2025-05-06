package org.noint.pickminbloom.exception.post;

import org.noint.pickminbloom.exception.PickminBloomException;

public class EncodeFailException extends PickminBloomException {
    public EncodeFailException() {
        super(500, "", "Base64 Encode Fail");
    }
}
