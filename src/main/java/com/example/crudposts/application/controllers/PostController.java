package com.example.crudposts.application.controllers;

import com.example.crudposts.domain.models.Comment;
import com.example.crudposts.domain.models.Post;
import com.example.crudposts.domain.services.CommentService;
import com.example.crudposts.domain.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.create(post));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable UUID id, @RequestBody Post post) {
        return ResponseEntity.ok(postService.update(id, post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<Void> archive(@PathVariable UUID id) {
        postService.archive(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/unarchive")
    public ResponseEntity<Void> unarchive(@PathVariable UUID id) {
        postService.unarchive(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> findComments(@PathVariable UUID id) {
        return ResponseEntity.ok(commentService.findByPostId(id));
    }
}