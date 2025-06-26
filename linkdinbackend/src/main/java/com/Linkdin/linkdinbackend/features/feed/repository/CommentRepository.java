package com.Linkdin.linkdinbackend.features.feed.repository;

import com.Linkdin.linkdinbackend.features.feed.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment ,Long> {
}
