package com.example.crudposts.domain.mappers;

import com.example.crudposts.domain.models.User;
import com.example.crudposts.domain.models.dtos.UserRequestDTO;
import com.example.crudposts.domain.models.dtos.UserResponseDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserWebMapper {

    public User toDomain(UserRequestDTO request) {
        if (request == null) return null;

        return new User(
                null,
                request.username(),
                request.name(),
                request.email(),
                request.password(),
                request.biography(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public UserResponseDTO toResponse(User domain) {
        if (domain == null) return null;

        return new UserResponseDTO(
                domain.id(),
                domain.username(),
                domain.name(),
                domain.email(),
                domain.biography()
        );
    }
}