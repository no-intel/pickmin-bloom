package org.noint.pickminbloom.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.post.entity.PrePost;
import org.noint.pickminbloom.post.enums.PrePostStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.noint.pickminbloom.post.entity.QPrePost.prePost;

@Repository
@RequiredArgsConstructor
public class PrePostQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<PrePost> findByCoor(Double latitude, Double longitude) {
        return Optional.ofNullable(queryFactory
                .selectFrom(prePost)
                .where(
                        prePost.latitude.eq(latitude),
                        prePost.longitude.eq(longitude),
                        prePost.status.ne(PrePostStatus.REJECTED)
                )
                .fetchFirst());
    }
}
