package org.noint.pickminbloom.post.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.exception.post.ImgToByteException;
import org.noint.pickminbloom.member.entity.Member;
import org.noint.pickminbloom.post.enums.PostType;
import org.noint.pickminbloom.post.enums.PrePostStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "pre_posts")
public class PrePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] img;

    @Column(nullable = false, name = "no_img")
    private boolean noImg;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "requested_by")
    private Member requester;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PrePostStatus status;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public PrePost(String name,
                   Double latitude,
                   Double longitude,
                   PostType type,
                   MultipartFile postImg,
                   boolean noImg,
                   Member requester) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        convertByte(postImg);
        this.noImg = noImg;
        this.requester = requester;
        this.status = PrePostStatus.WAITING;
        createdAt = LocalDateTime.now();
    }

    public void updateStatus(PrePostStatus status, Long updatedBy) {
        this.status = status;
        this.updatedBy = updatedBy;
        this.updatedAt = LocalDateTime.now();
    }

    private void convertByte(MultipartFile img) {
        try {
            this.img = img.getBytes();
        } catch (IOException e) {
            log.error("MultipartFile to byte error: {}", e.getMessage());
            throw new ImgToByteException();
        }
    }
}
