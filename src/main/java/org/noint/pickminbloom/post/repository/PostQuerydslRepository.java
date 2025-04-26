package org.noint.pickminbloom.post.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.noint.pickminbloom.post.dto.GetPostCoordinatesByViewDto;
import org.noint.pickminbloom.post.entity.Post;
import org.springframework.stereotype.Repository;

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

    public List<Post> findPostsByView(GetPostCoordinatesByViewDto dto) {
        return queryFactory
                .selectFrom(post)
                .where(
                        Expressions.numberTemplate(Double.class, "ST_Y({0})", post.coordinates)
                                .between(dto.getMinX(), dto.getMaxX()),
                        Expressions.numberTemplate(Double.class, "ST_X({0})", post.coordinates)
                                .between(dto.getMinY(), dto.getMaxY())
                )
                .fetch();
    }

}
