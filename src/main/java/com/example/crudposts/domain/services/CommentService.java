package com.example.crudposts.domain.services;

import com.example.crudposts.application.domain.models.Comment;
import com.example.crudposts.infra.mappers.CommentPersistenceMapper;
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

    @Transactional
    public Comment create(Comment commentDomain) {
        // Futuramente aqui validaremos se o Post existe e não está arquivado
        var entity = commentMapper.toEntity(commentDomain);
        return commentMapper.toDomain(commentRepository.save(entity));
    }

    @Transactional
    public Comment update(UUID id, Comment commentUpdate) {
        var entity = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado."));

        entity.setMessage(commentUpdate.message());
        return commentMapper.toDomain(commentRepository.save(entity));
    }

    @Transactional
    public void delete(UUID id) {
        if (!commentRepository.existsById(id)) throw new RuntimeException("Comentário não encontrado.");
        commentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Comment> findByPostId(UUID postId) {
        return commentRepository.findByPostId(postId)
                .stream()
                .map(commentMapper::toDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Comment> findUserCommentsInPublicPosts(UUID userId) {
        return commentRepository.findCommentsByUserIdInPublicPosts(userId)
                .stream()
                .map(commentMapper::toDomain)
                .toList();
    }
}