package org.noint.pickminbloom.post.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.exception.post.ImgToByteException;
import org.noint.pickminbloom.member.entity.Member;
import org.noint.pickminbloom.post.enums.PostEditRequestStatus;
import org.noint.pickminbloom.post.enums.PostType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post_edit_reqeusts")
@ToString
public class PostEditRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "post_id")
    private Post post;

    @Column(nullable = false, name = "edit_name")
    private String editName;

    @Column(nullable = false, name = "edit_latitude")
    private Double editLatitude;

    @Column(nullable = false, name = "edit_longitude")
    private Double editLongitude;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB", name = "edit_img")
    private byte[] editImg;

    @Column(nullable = false, name = "edit_type")
    @Enumerated(EnumType.STRING)
    private PostType editType;

    @Column(nullable = false, name = "edit_status")
    @Enumerated(EnumType.STRING)
    private PostEditRequestStatus editStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "requested_by")
    private Member requester;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public PostEditRequest(Post post,
                           String editName,
                           Double editLatitude,
                           Double editLongitude,
                           MultipartFile editImg,
                           PostType editType,
                           Member requester) {
        this.post = post;
        this.editName = editName;
        this.editLatitude = editLatitude;
        this.editLongitude = editLongitude;
        convertByte(editImg);
        this.editType = editType;
        this.editStatus = PostEditRequestStatus.WAITING;
        this.requester = requester;
        this.createdAt = LocalDateTime.now();
    }

    private void convertByte(MultipartFile img) {
        try {
            if (img == null) {
                this.editImg = null;
            }else {
                this.editImg = img.getBytes();
            }
        } catch (IOException e) {
            log.error("MultipartFile to byte error: {}", e.getMessage());
            throw new ImgToByteException();
        }
    }

    public void confirm(PostEditRequestStatus editStatus , Long updatedBy) {
        this.editStatus = editStatus;
        this.updatedBy = updatedBy;
        this.updatedAt = LocalDateTime.now();
    }
}
