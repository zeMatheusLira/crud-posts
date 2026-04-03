package com.example.crudposts.application.controllers;

import com.example.crudposts.application.domain.models.Comment;
import com.example.crudposts.application.domain.models.Post;
import com.example.crudposts.application.domain.models.User;
import com.example.crudposts.domain.services.CommentService;
import com.example.crudposts.domain.services.PostService;
import com.example.crudposts.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable UUID id, @RequestBody User user) {
        return ResponseEntity.ok(userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<Post>> findPublicPosts(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.findPublicPostsByUserId(id));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> findCommentsInPublicPosts(@PathVariable UUID id) {
        return ResponseEntity.ok(commentService.findUserCommentsInPublicPosts(id));
    }
}