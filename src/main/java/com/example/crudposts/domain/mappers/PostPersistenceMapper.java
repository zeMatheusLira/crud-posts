package com.example.crudposts.domain.mappers;

import com.example.crudposts.domain.models.Post;
import com.example.crudposts.domain.entities.PostEntity;
import org.springframework.stereotype.Component;

@Component
public class PostPersistenceMapper {

    public PostEntity toEntity(Post domain){
        if (domain == null) return null;

        return new PostEntity(
                domain.id(),
                domain.userId(),
                domain.text(),
                domain.archived(),
                domain.createdAt(),
                domain.updatedAt()
        );
    }

    public Post toDomain(PostEntity entity) {
        if (entity == null) return null;

        return new Post(
                entity.getId(),
                entity.getUserId(),
                entity.getText(),
                entity.isArchived(),
                entity.getCreatedAt() != null ? entity.getCreatedAt() : java.time.LocalDateTime.now(),
                entity.getUpdatedAt() != null ? entity.getUpdatedAt() : java.time.LocalDateTime.now()
        );
    }
}
