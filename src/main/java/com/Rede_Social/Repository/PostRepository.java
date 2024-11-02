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

    @Query(value = "SELECT p.* FROM post p " + "LEFT JOIN likee l ON p.uuid = l.post_id " + "GROUP BY p.uuid " + "ORDER BY COUNT(l.uuid) DESC " + "LIMIT 10", nativeQuery = true)
    List<PostEntity> Top10PostsComLike();

    @Query(value = "SELECT p.* " + "FROM post p " + "JOIN like l ON p.id = l.post_id " + "WHERE l.data_like >= CURRENT_DATE - INTERVAL 7 DAY " + "GROUP BY p.id " + "ORDER BY COUNT(l.id) DESC LIMIT 1", nativeQuery = true)
    PostEntity findByMaisCurtido();

    @Query(value = "SELECT p.* FROM post p " + "WHERE p.valido = true " + "ORDER BY p.data DESC " + "LIMIT 50", nativeQuery = true)
    List<PostEntity> PostsValidos();

}
