package com.example.crudposts.domain.mappers;

import com.example.crudposts.domain.models.Comment;
import com.example.crudposts.domain.entities.CommentEntity;
import org.springframework.stereotype.Component;

@Component
public class CommentPersistenceMapper {

    public CommentEntity toEntity(Comment domain) {
        if (domain == null) return null;

        return new CommentEntity(
                domain.id(),
                domain.userId(),
                domain.postId(),
                domain.message(),
                domain.createdAt(),
                domain.updatedAt()
        );
    }

    public Comment toDomain(CommentEntity entity) {
        if (entity == null) return null;

        return new Comment(
                entity.getId(),
                entity.getUserId(),
                entity.getPostId(),
                entity.getMessage(),
                entity.getCreatedAt() != null ? entity.getCreatedAt() : java.time.LocalDateTime.now(),
                entity.getUpdatedAt() != null ? entity.getUpdatedAt() : java.time.LocalDateTime.now()
        );
    }
}
