package org.noint.pickminbloom.post.repository;

import org.noint.pickminbloom.post.entity.PrePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrePostRepository extends JpaRepository<PrePost, Long> {
}
