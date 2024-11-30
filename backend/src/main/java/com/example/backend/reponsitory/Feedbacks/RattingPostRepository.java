package com.example.backend.reponsitory.Feedbacks;

import com.example.backend.enity.RattingPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RattingPostRepository extends JpaRepository<RattingPost, Long> {

    List<RattingPost> findByPostId(Long post_id);

    Optional<RattingPost> findByUserIdAndPostId(Long userIdm ,Long postId);
}
