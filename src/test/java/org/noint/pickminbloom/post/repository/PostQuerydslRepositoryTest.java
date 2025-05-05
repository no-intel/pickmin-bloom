package org.noint.pickminbloom.post.repository;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.noint.pickminbloom.post.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class PostQuerydslRepositoryTest {

    @Autowired
    private PostQuerydslRepository postQuerydslRepository;

    @Test
    void Querdsl_Spatial_조회_테스트() throws Exception {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(127.1332864, 37.453824));
        List<Post> postsWithinDistance = postQuerydslRepository.findPostsWithinDistance(point, 200);
        System.out.println(point);
    }

}