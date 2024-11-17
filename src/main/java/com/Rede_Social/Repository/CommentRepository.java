package com.Rede_Social.Repository;

import com.Rede_Social.Entity.CommentEntity;
import com.Rede_Social.Entity.LikeEntity;
import com.Rede_Social.Entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {
    @Query(value = "SELECT c.* FROM comment c " +
            "WHERE c.valido = true " +
            "AND c.post_id = :postId " +
            "ORDER BY c.data DESC " +
            "LIMIT 10", nativeQuery = true)
    List<CommentEntity> commentsValidos(@Param("postId") UUID post_id);

}
