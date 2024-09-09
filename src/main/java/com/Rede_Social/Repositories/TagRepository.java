package com.Rede_Social.Repositories;

import com.Rede_Social.Entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
}
