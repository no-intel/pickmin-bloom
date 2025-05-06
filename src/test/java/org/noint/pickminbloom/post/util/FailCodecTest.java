package org.noint.pickminbloom.post.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.noint.pickminbloom.exception.post.EmptyFailException;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class FailCodecTest {

    private FileCodecUtil fileCodecUtil;

    @BeforeEach
    void setUp() {
        fileCodecUtil = new FileCodecUtil();
    }


    @Test
    void 이미지_파일_인코딩() throws Exception {
        // given
        byte[] fileContent = new byte[]{1, 2, 3, 4, 5};
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                "sample-image.png",
                "image/png",
                fileContent
        );

        // when
        String encodedString = fileCodecUtil.encode(mockMultipartFile);

        // then
        String expectedEncodedString = Base64.getEncoder().encodeToString(fileContent);
        assertNotNull(encodedString);
        assertEquals(expectedEncodedString, encodedString);
    }

    @Test
    void 이미지_파일_인코딩_실패() throws Exception {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                "empty-image.png",
                "image/png",
                new byte[0]
        );

        // when&then
        assertThrows(EmptyFailException.class, () -> fileCodecUtil.encode(mockMultipartFile));
    }
}