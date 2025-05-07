package org.noint.pickminbloom.post.util;

import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.exception.post.DecodeFailException;
import org.noint.pickminbloom.exception.post.EmptyFailException;
import org.noint.pickminbloom.exception.post.EncodeFailException;
import org.noint.pickminbloom.exception.post.NotImgException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@Component
public class FileCodecUtil {

    public String encode(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new EmptyFailException();
            }

            byte[] fileBytes = file.getBytes();
            return Base64.getEncoder().encodeToString(fileBytes);
        } catch (IOException e) {
            log.error("Base64 encode error: {}", e.getMessage());
            throw new EncodeFailException();
        }
    }

    public MultipartFile decodeToMultipartFile(String geohash, String base64) {
        try {
            byte[] imageBytes = decodeBase64(base64);

            // 이미지의 content-type과 확장자 자동 판별
            String[] imageInfo = detectContentTypeAndExtension(imageBytes);
            String contentType = imageInfo[0];
            String extension = imageInfo[1];

            String filename = geohash + "." + extension;
            return new CustomMultipartFile(imageBytes, "file", filename, contentType);
        } catch (IOException e) {
            log.error("Base64 decode error: {}", e.getMessage());
            throw new DecodeFailException();
        }
    }

    // MAME 헤더(확장자)가 있다면 제거
    private static byte[] decodeBase64(String base64) {
        String pureBase64 = base64.contains(",") ? base64.split(",")[1] : base64;
        return Base64.getDecoder().decode(pureBase64);
    }

    // 바이트 코드 통해서 확장자 설정
    private String[] detectContentTypeAndExtension(byte[] imageBytes) throws IOException {
        // PNG
        if (imageBytes.length >= 8 &&
                (imageBytes[0] & 0xFF) == 0x89 &&
                (imageBytes[1] & 0xFF) == 0x50 &&
                (imageBytes[2] & 0xFF) == 0x4E &&
                (imageBytes[3] & 0xFF) == 0x47 &&
                (imageBytes[4] & 0xFF) == 0x0D &&
                (imageBytes[5] & 0xFF) == 0x0A &&
                (imageBytes[6] & 0xFF) == 0x1A &&
                (imageBytes[7] & 0xFF) == 0x0A) {
            return new String[]{"image/png", "png"};
        }

        // JPG / JPEG
        if (imageBytes.length >= 3 &&
                (imageBytes[0] & 0xFF) == 0xFF &&
                (imageBytes[1] & 0xFF) == 0xD8 &&
                (imageBytes[2] & 0xFF) == 0xFF) {
            return new String[]{"image/jpeg", "jpeg"};
        }
        throw new NotImgException();
    }
}
