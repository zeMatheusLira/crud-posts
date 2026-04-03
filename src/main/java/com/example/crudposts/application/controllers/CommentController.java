package com.example.crudposts.application.controllers;

import com.example.crudposts.application.domain.models.Comment;
import com.example.crudposts.domain.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> create(@RequestBody Comment comment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.create(comment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> update(@PathVariable UUID id, @RequestBody Comment comment) {
        return ResponseEntity.ok(commentService.update(id, comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}