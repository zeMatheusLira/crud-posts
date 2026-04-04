package com.example.crudposts.domain.mappers;

import com.example.crudposts.domain.models.User;
import com.example.crudposts.domain.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public UserEntity toEntity(User domain){
        if (domain == null) return null;

        return new UserEntity(
                domain.id(),
                domain.username(),
                domain.name(),
                domain.email(),
                domain.password(),
                domain.biography(),
                domain.createdAt(),
                domain.updatedAt()
        );
    }

    public User toDomain(UserEntity entity) {
        if (entity == null) return null;

        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getBiography(),
                entity.getCreatedAt() != null ? entity.getCreatedAt() : java.time.LocalDateTime.now(),
                entity.getUpdatedAt() != null ? entity.getUpdatedAt() : java.time.LocalDateTime.now()
        );
    }
}
