package org.noint.pickminbloom.exception.post;

import lombok.Getter;
import org.noint.pickminbloom.exception.PickminBloomException;

@Getter
public class S3UploadException extends PickminBloomException {
    public S3UploadException() {
        super(500, "", "S3 업로드 실패.");
    }
}
