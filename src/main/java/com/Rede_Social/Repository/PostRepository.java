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
}
