package com.example.crudposts.infra.mappers;

import com.example.crudposts.application.domain.models.Post;
import com.example.crudposts.infra.entities.PostEntity;
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
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
