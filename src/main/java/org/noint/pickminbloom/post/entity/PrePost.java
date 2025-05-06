package org.noint.pickminbloom.post.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;
import org.noint.pickminbloom.post.enums.PostType;
import org.noint.pickminbloom.post.enums.PrePostStatus;

import java.time.LocalDateTime;

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

    @Column(columnDefinition = "TEXT")
    private String img;

    @Column(name = "no_img")
    private boolean noImg;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostType type;

    @Column(nullable = false, name = "requester_id")
    private Long requesterId;

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
                   String postImg,
                   boolean noImg,
                   Long requesterId) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.img = postImg;
        this.noImg = noImg;
        this.requesterId = requesterId;
        this.status = PrePostStatus.WAITING;
        createdAt = LocalDateTime.now();
    }
}
