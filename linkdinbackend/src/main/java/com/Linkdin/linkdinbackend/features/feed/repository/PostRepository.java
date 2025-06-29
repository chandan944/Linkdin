package com.Linkdin.linkdinbackend.features.feed.repository;

import com.Linkdin.linkdinbackend.features.feed.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorId(Long authorId);
    List<Post> findAllByOrderByCreationDateDesc();
    List<Post> findByAuthorIdNotOrderByCreationDateDesc(Long authenticatedUserId);
}
