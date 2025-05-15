package org.noint.pickminbloom.post.util.s3;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.exception.post.S3UploadException;
import org.noint.pickminbloom.post.dto.GetPostResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Profile("!local")
@Slf4j
@Component
@Transactional
public class AwsS3Util implements S3util {
    private S3Client s3Client;
    private S3Presigner s3Presigner;

    @Value("${aws.s3.bucket.post}")
    String bucketName;

    @Value("${aws.s3.region}")
    String region;

    @Value("${aws.s3.presigned.duration}")
    String presignedDuration;

    @Value("${aws.s3.publicBaseUrl}")
    String publicBaseUrl;

    @Value("${aws.s3.credentials.access-key}")
    String accessKey;

    @Value("${aws.s3.credentials.secret-key}")
    String secretKey;

    @PostConstruct
    public void init() {
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();

        this.s3Presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .serviceConfiguration(
                        S3Configuration.builder()
                                .pathStyleAccessEnabled(true)
                                .build()
                )
                .build();

        createBucketIfNotExists();
    }

    private void createBucketIfNotExists() {
        try {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            CreateBucketResponse response = s3Client.createBucket(createBucketRequest);
            log.info("Bucket created: {}", response.location());
        } catch (Exception e) {
            log.warn("Bucket already exists or error: {}", e.getMessage());
        }
    }

    public void uploadFile(String geohash, MultipartFile file){
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(geohash)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );
        } catch (IOException e) {
            log.error("S3 upload error: {}", e.getMessage());
            throw new S3UploadException();
        }
    }

    public Map<String, String> getDownloadUrl(List<GetPostResponseDto> posts) {
        return posts.stream()
                .collect(Collectors.toMap(
                        GetPostResponseDto::geohash,
                        post -> post.noImg() ? null : publicBaseUrl + "/" + post.geohash()
                ));

    }

    // 버킷 파일 비공개시 사용
    public Map<String, String> createdPresignedUrlsForDownload(List<String> geohashes) {
        return geohashes.stream()
                .collect(Collectors.toMap(
                        geohash -> geohash,
                        geohash -> {
                            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                                    .bucket(bucketName)
                                    .key(geohash)
                                    .build();

                            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                                    .signatureDuration(Duration.ofMinutes(Long.parseLong(presignedDuration)))
                                    .getObjectRequest(getObjectRequest)
                                    .build();

                            return s3Presigner.presignGetObject(presignRequest).url().toString();
                        }
                ));
    }
}
