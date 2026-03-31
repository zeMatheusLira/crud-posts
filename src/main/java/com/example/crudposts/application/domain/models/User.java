package com.example.crudposts.application.domain.models;

import java.time.LocalDateTime;

public record User(
        Long id,
        String username,
        String name,
        String email,
        String password,
        String biography,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}