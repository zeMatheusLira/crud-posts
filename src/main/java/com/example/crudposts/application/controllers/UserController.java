package com.example.crudposts.application.controllers;

import com.example.crudposts.domain.mappers.UserWebMapper;
import com.example.crudposts.domain.models.Comment;
import com.example.crudposts.domain.models.Post;
import com.example.crudposts.domain.models.User;
import com.example.crudposts.domain.models.dtos.UserRequestDTO;
import com.example.crudposts.domain.models.dtos.UserResponseDTO;
import com.example.crudposts.domain.services.CommentService;
import com.example.crudposts.domain.services.PostService;
import com.example.crudposts.domain.services.UserService;
import jakarta.validation.Valid;
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
    private final UserWebMapper webMapper;

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserRequestDTO request) {
        User domain = webMapper.toDomain(request);
        User savedUser = userService.create(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(webMapper.toResponse(savedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable UUID id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(webMapper.toResponse(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid UserRequestDTO request) {
        User domain = webMapper.toDomain(request);
        User updatedUser = userService.update(id, domain);
        return ResponseEntity.ok(webMapper.toResponse(updatedUser));
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