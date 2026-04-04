package com.example.crudposts.domain.services;

import com.example.crudposts.domain.models.Comment;
import com.example.crudposts.exceptions.custom.BusinessException;
import com.example.crudposts.exceptions.custom.EntityNotFoundException;
import com.example.crudposts.domain.entities.CommentEntity;
import com.example.crudposts.domain.mappers.CommentPersistenceMapper;
import com.example.crudposts.infra.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentPersistenceMapper commentMapper;
    private final UserService userService;
    private final PostService postService;

    @Transactional
    public Comment create(Comment commentDomain) {
        userService.findById(commentDomain.userId());

        var post = postService.findById(commentDomain.postId());

        if (post.archived()) {
            throw new BusinessException("Não é permitido comentar em publicações arquivadas.");
        }

        var entity = commentMapper.toEntity(commentDomain);
        var savedEntity = commentRepository.save(entity);
        return commentMapper.toDomain(savedEntity);
    }

    @Transactional
    public Comment update(UUID id, Comment commentUpdate) {
        CommentEntity entity = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comentário não encontrado para edição."));

        entity.setMessage(commentUpdate.message());
        return commentMapper.toDomain(commentRepository.save(entity));
    }

    @Transactional
    public void delete(UUID id) {
        if (!commentRepository.existsById(id)) {
            throw new EntityNotFoundException("Comentário inexistente.");
        }
        commentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Comment> findByPostId(UUID postId) {
        postService.findById(postId);

        return commentRepository.findByPostId(postId)
                .stream()
                .map(commentMapper::toDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Comment> findUserCommentsInPublicPosts(UUID userId) {
        userService.findById(userId);

        return commentRepository.findCommentsByUserIdInPublicPosts(userId)
                .stream()
                .map(commentMapper::toDomain)
                .toList();
    }
}