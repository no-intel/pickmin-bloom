package org.noint.pickminbloom.exception.post;

import org.noint.pickminbloom.exception.PickminBloomException;

public class NotImgException extends PickminBloomException {
    public NotImgException() {
        super(500, "file", "이미지 파일이 아닙니다. JPEG, PNG, JPG, WEBP만 허용합니다.");
    }
}
