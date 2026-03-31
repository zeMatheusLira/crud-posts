package com.example.crudposts.application.domain.models;

import java.time.LocalDateTime;

public record Comment(
        Long id,
        Long userId,
        Long postId,
        String message,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
