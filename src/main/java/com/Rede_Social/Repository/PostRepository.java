package com.Rede_Social.Repository;

import com.Rede_Social.Entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, UUID> {
    List<PostEntity> findByTagsNome(String nome);

    @Query(value = "SELECT p.* FROM post p " + "LEFT JOIN likee l ON p.uuid = l.post_id " + "WHERE p.valido = true " + "GROUP BY p.uuid " + "ORDER BY COUNT(l.uuid) DESC " + "LIMIT 10", nativeQuery = true)
    List<PostEntity> Top10PostsComLike();

    @Query(value = "SELECT p.* " + "FROM post p " + "JOIN like l ON p.id = l.post_id " + "WHERE l.data_like >= CURRENT_DATE - INTERVAL 7 DAY " + "GROUP BY p.id " + "ORDER BY COUNT(l.id) DESC LIMIT 1", nativeQuery = true)
    PostEntity findByMaisCurtido();

    @Query(value = """
    SELECT DISTINCT p.* 
    FROM post p
    LEFT JOIN comment c ON p.uuid = c.post_id
    WHERE p.valido = true AND (c.valido = true OR c.valido IS NULL)
    """, nativeQuery = true)
    List<PostEntity> PostsValidos();
}
