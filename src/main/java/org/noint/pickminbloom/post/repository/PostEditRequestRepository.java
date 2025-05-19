package org.noint.pickminbloom.post.repository;

import org.noint.pickminbloom.post.entity.PostEditRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostEditRequestRepository extends JpaRepository<PostEditRequest, Long> {
}
