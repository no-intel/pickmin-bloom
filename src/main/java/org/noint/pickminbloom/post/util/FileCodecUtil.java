package org.noint.pickminbloom.post.util;

import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.exception.post.EmptyFailException;
import org.noint.pickminbloom.exception.post.EncodeFailException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@Component
public class FileCodecUtil {

    public String encode(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) { // 빈 파일 또는 null 확인
                throw new EmptyFailException();
            }

            byte[] fileBytes = file.getBytes();
            return Base64.getEncoder().encodeToString(fileBytes);
        } catch (IOException e) {
            log.error("Base64 encode error: {}", e.getMessage());
            throw new EncodeFailException();
        }
    }

    private byte[] decode(String base64) {
        return Base64.getDecoder().decode(base64);
    }
}
