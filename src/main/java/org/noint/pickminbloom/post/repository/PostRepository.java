package org.noint.pickminbloom.post.repository;

import org.noint.pickminbloom.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
