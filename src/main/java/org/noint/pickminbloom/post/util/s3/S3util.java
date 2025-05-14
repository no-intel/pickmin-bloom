package org.noint.pickminbloom.post.util.s3;

import org.noint.pickminbloom.post.dto.GetPostResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface S3util {
    void uploadFile(String geohash, MultipartFile file);

    Map<String, String> getDownloadUrl(List<GetPostResponseDto> geohashes);

    Map<String, String> createdPresignedUrlsForDownload(List<String> geohashes);
}
