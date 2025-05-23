package org.noint.pickminbloom.post.util.s3;

import org.noint.pickminbloom.post.dto.GetPostResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface S3util {
    void uploadFile(String key, MultipartFile file);

    Map<String, String> getDownloadUrl(List<GetPostResponseDto> posts);

    Map<String, String> createdPresignedUrlsForDownload(List<String> keys);

    void renameFile(String oldKey, String newKey);

    void deleteFile(String key);
}
