package com.example.crudposts.infra.repository;

import com.example.crudposts.infra.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {
    List<CommentEntity> findByPostId(UUID postId);

    @Query("SELECT c FROM CommentEntity c JOIN PostEntity p ON c.postId = p.id " +
            "WHERE c.userId = :userId AND p.archived = false")
    List<CommentEntity> findCommentsByUserIdInPublicPosts(@Param("userId") UUID userId);
}