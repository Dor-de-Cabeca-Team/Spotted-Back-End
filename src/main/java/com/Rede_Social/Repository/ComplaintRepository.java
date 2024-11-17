package com.Rede_Social.Repository;

import com.Rede_Social.Entity.ComplaintEntity;
import com.Rede_Social.Entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ComplaintRepository extends JpaRepository<ComplaintEntity, UUID> {
    @Query(value = "SELECT * FROM complaint WHERE complaint.post_id = :postId AND complaint.user_id = :userId AND complaint.comment_id IS NULL", nativeQuery = true)
    Optional<ComplaintEntity> findByPostAndUser(UUID postId, UUID userId);

    @Query(value = "SELECT * FROM complaint WHERE complaint.comment_id = :commentId AND complaint.user_id = :userId", nativeQuery = true)
    Optional<ComplaintEntity> findByCommentAndUser(UUID commentId, UUID userId);
}
