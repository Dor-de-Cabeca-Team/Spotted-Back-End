package com.Rede_Social.Repository;

import com.Rede_Social.Entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, UUID> {
    List<PostEntity> findByTagsNome(String nome);

    @Query(value = "SELECT p.* FROM post p " +
            "LEFT JOIN likee l ON p.uuid = l.post_id " +
            "WHERE p.valido = true " +
            "AND l.data >= CURRENT_DATE - INTERVAL '7 days' " +
            "GROUP BY p.uuid " +
            "ORDER BY COUNT(l.uuid) DESC " +
            "LIMIT 10",
            nativeQuery = true)
    List<PostEntity> Top10PostsComLike();

    @Query(value = """
    SELECT DISTINCT p.* 
    FROM post p
    LEFT JOIN comment c ON p.uuid = c.post_id
    WHERE p.valido = true
    ORDER BY p.data DESC
    """,
            countQuery = """
    SELECT COUNT(DISTINCT p.uuid)
    FROM post p
    LEFT JOIN comment c ON p.uuid = c.post_id
    WHERE p.valido = true
    """,
            nativeQuery = true)
    Page<PostEntity> PostsValidos(Pageable pageable);
}
