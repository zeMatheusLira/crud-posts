package com.example.crudposts.infra.repository;

import com.example.crudposts.domain.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, UUID> {
    List<PostEntity> findByUserIdAndArchivedFalse(UUID userId);
}
