package com.Rede_Social.Repositories;

import com.Rede_Social.Entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

}
