package com.example.crudposts.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

public record User(
        UUID id,
        String username,
        String name,
        String email,
        String password,
        String biography,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}