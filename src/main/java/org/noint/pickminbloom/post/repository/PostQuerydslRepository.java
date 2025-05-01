package org.noint.pickminbloom.post.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.noint.pickminbloom.post.dto.GetPostCoordinatesByViewDto;
import org.noint.pickminbloom.post.dto.GetPostResponseDto;
import org.noint.pickminbloom.post.entity.Post;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.noint.pickminbloom.post.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public List<Post> findPostsWithinDistance(Point referencePoint, Integer distanceInMeters) {
        return queryFactory
                .selectFrom(post)
                .where(
                        Expressions.booleanTemplate(
                                "ST_DISTANCE_SPHERE({0}, {1}) <= {2}",
                                post.coordinates,
                                referencePoint,
                                distanceInMeters
                        )
                ).orderBy(Expressions.numberTemplate(
                        Double.class,
                        "ST_DISTANCE_SPHERE({0}, {1})",
                        post.coordinates,
                        referencePoint
                ).asc())
                .fetch();
    }

    public List<GetPostResponseDto> findPostsByView(GetPostCoordinatesByViewDto dto) {
        String pointWKT = String.format("POINT(%f %f)", dto.latitude(), dto.longitude());

        StringTemplate distance = Expressions.stringTemplate(
                "ST_Distance_Sphere({0}, ST_GeomFromText({1}, 4326))",
                post.coordinates,
                pointWKT
        );

        return queryFactory
                .select(Projections.constructor(GetPostResponseDto.class,
                        post.id,
                        post.geohash,
                        post.name,
                        post.coordinates,
                        post.location,
                        post.type,
                        post.createdAt,
                        distance.castToNum(Double.class).as("distance")
                        ))
                .from(post)
                .where(
                        Expressions.numberTemplate(Double.class, "ST_Y({0})", post.coordinates)
                                .between(dto.minLongitude(), dto.maxLongitude()),
                        Expressions.numberTemplate(Double.class, "ST_X({0})", post.coordinates)
                                .between(dto.minLatitude(), dto.maxLatitude())
                )
                .orderBy(distance.asc())
                .limit(20)
                .fetch();
    }

}
