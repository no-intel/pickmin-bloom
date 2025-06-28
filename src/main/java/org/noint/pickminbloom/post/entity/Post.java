package org.noint.pickminbloom.post.entity;

import jakarta.persistence.*;
import lombok.*;
import org.noint.pickminbloom.member.entity.Member;
import org.noint.pickminbloom.post.enums.PostType;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "posts")
@ToString
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, name = "geohash", length = 12)
    private String geohash;

    @Column(name = "name")
    private String name;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostType type;

    @Column(nullable = false, name = "no_img")
    private boolean noImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "requested_by")
    private Member requester;

    @Column(nullable = false, name = "confirmed_by")
    private Long confirmedBy;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Post(String geohash,
                String name,
                Double latitude,
                Double longitude,
                PostType type,
                Member requester,
                boolean noImg,
                Long confirmedBy) {
        this.geohash = geohash;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.noImg = noImg;
        this.createdAt = LocalDateTime.now();
        this.requester = requester;
        this.confirmedBy = confirmedBy;
    };

    public void editPost(String editName,
                         PostType editType) {
        this.name = editName;
        this.type = editType;
        this.updatedAt = LocalDateTime.now();
    }
}
