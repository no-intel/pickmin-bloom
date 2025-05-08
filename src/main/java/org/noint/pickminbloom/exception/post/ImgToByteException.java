package org.noint.pickminbloom.exception.post;

import org.noint.pickminbloom.exception.PickminBloomException;

public class ImgToByteException extends PickminBloomException {
    public ImgToByteException() {
        super(500, "img", "이미지 파일을 바이트 배열로 변환하는데 실패했습니다.");
    }
}
