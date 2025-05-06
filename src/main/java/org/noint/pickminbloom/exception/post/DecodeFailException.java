package org.noint.pickminbloom.exception.post;

import org.noint.pickminbloom.exception.PickminBloomException;

public class DecodeFailException extends PickminBloomException {
    public DecodeFailException() {
        super(500, "", "Base64 Decode Fail");
    }
}
