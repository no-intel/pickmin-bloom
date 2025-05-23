package org.noint.pickminbloom.exception.s3;

import org.noint.pickminbloom.exception.PickminBloomException;

public class S3TaskException extends PickminBloomException {
    public S3TaskException(String task) {
        super(404, task, "S3 작업 실패");
    }
}
