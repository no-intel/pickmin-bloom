package org.noint.pickminbloom.post.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.post.dto.GetPostCoordinatesByViewDto;
import org.noint.pickminbloom.post.dto.GetPostResponseDto;
import org.noint.pickminbloom.post.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.noint.pickminbloom.post.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public List<GetPostResponseDto> findPostsByView(GetPostCoordinatesByViewDto dto) {
        // 중심 좌표를 WKT 형식으로 변환 (mysql 순서: 위도, 경도)
        String wkt = String.format("POINT(%f %f)", dto.latitude(), dto.longitude());

        // 거리 계산용 식 (순서 바뀜: 위도, 경도)
        StringTemplate distanceExpr = Expressions.stringTemplate(
                "ST_Distance_Sphere(ST_GeomFromText(CONCAT('POINT(', {1}, ' ', {0}, ')'), 4326), ST_GeomFromText({2}, 4326))",
                post.longitude,
                post.latitude,
                wkt
        );

        return queryFactory
                .select(Projections.constructor(GetPostResponseDto.class,
                        post.id,
                        post.geohash,
                        post.name,
                        post.latitude,
                        post.longitude,
                        post.type,
                        post.noImg,
                        post.createdAt,
                        distanceExpr.castToNum(Double.class).as("distance")
                ))
                .from(post)
                .where(
                        post.longitude.between(dto.minLongitude(), dto.maxLongitude()),
                        post.latitude.between(dto.minLatitude(), dto.maxLatitude()),
                        post.deletedAt.isNull()
                )
                .orderBy(distanceExpr.asc())
                .limit(20)
                .fetch();
    }

    public Optional<Post> findByCoor(Double latitude, Double longitude) {
        return Optional.ofNullable(queryFactory
                .selectFrom(post)
                .where(
                        post.latitude.eq(latitude),
                        post.longitude.eq(longitude),
                        post.deletedAt.isNull()
                )
                .fetchFirst());
    }
}
