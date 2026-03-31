package com.example.crudposts.application.domain.models;

import java.time.LocalDateTime;

public record Post(
        Long id,
        Long userId,
        String text,
        boolean archived,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public Post withArchived(boolean archived) {
        return new Post(id, userId, text, archived, createdAt, LocalDateTime.now());
    }
}