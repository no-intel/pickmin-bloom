package org.noint.pickminbloom.post.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.noint.pickminbloom.exception.post.S3UploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class S3UtilTest {

    @Value("${aws.s3.endpoint}")
    private String endpoint;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.bucket.post}")
    private String bucketName;

    @Value("${aws.s3.presigned.duration}")
    String presignedDuration;

    @Value("${aws.s3.credentials.access-key}")
    private String accessKey;

    @Value("${aws.s3.credentials.secret-key}")
    private String secretKey;

    private S3Client s3Client;

    private S3Presigner s3Presigner;

    @BeforeEach
    void setUp() {
        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .forcePathStyle(true)
                .build();

        this.s3Presigner = S3Presigner.builder()
                .endpointOverride(URI.create(endpoint))
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
    }


    @Test
    void 이미지_파일_업로드() throws Exception {
        //given
        MockMultipartFile imageFile = new MockMultipartFile(
                "file",
                "sample-image.png",
                "image/png",
                new byte[] {1, 2, 3, 4, 5}
        );

        String geohash = "testtesttest";

        //when
        uploadFile(geohash, imageFile);

        boolean objectExists = s3Client.listObjects(ListObjectsRequest.builder().bucket(bucketName).build())
                .contents()
                .stream()
                .anyMatch(s3Object -> s3Object.key().equals(geohash));

        //then
        assertTrue(objectExists);
    }

    @Test
    void 이미지_파일_업로드_실패() {
        //given
        MultipartFile imageFile = new MockMultipartFile("file", "sample-image.png", "image/png", new byte[]{1, 2, 3, 4, 5}) {
            @Override
            public byte[] getBytes() throws IOException {
                throw new IOException("IOException 발생");
            }
        };

        String geohash = "failtest";

        //when, then
        assertThrows(RuntimeException.class, () -> uploadFile(geohash, imageFile));
    }

    @Test
    void 이미지_다운로드_PresignedUrl() {
        //given
        List<String> geohashes = new ArrayList<>(){{
            add("geohashesTest1");
            add("geohashesTest2");
        }};

        //when
        Map<String, String> presignedUrls = createdPresignedUrlsForDownload(geohashes);

        //then
        assertEquals(presignedUrls.size(), geohashes.size());
    }

    private void uploadFile(String geohash, MultipartFile file){
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
            throw new S3UploadException();
        }
    }

    private Map<String, String> createdPresignedUrlsForDownload(List<String> geohashes) {
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