package com.Rede_Social.Repositories;

import com.Rede_Social.Entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioRepository extends JpaRepository<PostEntity, Long> {

}
