package org.noint.pickminbloom.post.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

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

    @Column(nullable = false, name = "coordinates", columnDefinition = "POINT")
    @JdbcTypeCode(SqlTypes.GEOMETRY)
    private Point coordinates;

    @Column(name = "location")
    private String location;

    @Column(nullable = false, name = "type")
    private String type;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Post(String geohash,
                String name,
                Point coordinates,
                String location,
                String type) {
        this.geohash = geohash;
        this.name = name;
        initializeCoordinates(coordinates);
        this.location = location;
        this.type = type;
        this.createdAt = LocalDateTime.now();
    };

    private void initializeCoordinates(Point coordinates) {
        this.coordinates = coordinates;
        coordinates.setSRID(4326);
    }
}
