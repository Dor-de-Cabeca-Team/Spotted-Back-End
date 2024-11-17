package com.Rede_Social.Repository;

import com.Rede_Social.Entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, UUID> {
    @Query(value = "SELECT * FROM likee WHERE likee.post_id = :postId AND likee.user_id = :userId AND likee.comment_id IS NULL", nativeQuery = true)
    Optional<LikeEntity> findByPostAndUser(UUID postId, UUID userId);

    @Query(value = "SELECT * FROM likee WHERE likee.comment_id = :commentId AND likee.user_id = :userId", nativeQuery = true)
    Optional<LikeEntity> findByCommentAndUser(UUID commentId, UUID userId);
}
