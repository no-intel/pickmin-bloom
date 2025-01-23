package org.noint.pickminbloom.post.repository;

import org.junit.jupiter.api.Test;
import org.noint.pickminbloom.post.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void 엽서_테이블_테스트() throws Exception {
        Post post = new Post("38.111", "140.111", "M", LocalDateTime.now());

        postRepository.save(post);

        postRepository.findAll().forEach(System.out::println);
    }
}