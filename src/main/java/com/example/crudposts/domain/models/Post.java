package com.example.crudposts.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

public record Post(
        UUID id,
        UUID userId,
        String text,
        boolean archived,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public Post withArchived(boolean archived) {
        return new Post(id, userId, text, archived, createdAt, LocalDateTime.now());
    }
}