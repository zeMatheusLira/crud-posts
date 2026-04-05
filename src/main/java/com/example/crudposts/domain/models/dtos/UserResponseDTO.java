package com.example.crudposts.domain.models.dtos;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        String name,
        String email,
        String biography
) {
}