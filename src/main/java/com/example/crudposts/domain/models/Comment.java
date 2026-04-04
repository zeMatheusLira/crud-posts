package com.example.crudposts.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

public record Comment(
        UUID id,
        UUID userId,
        UUID postId,
        String message,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
