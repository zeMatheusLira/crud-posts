package com.example.crudposts.domain.services;

import com.example.crudposts.application.domain.models.Post;
import com.example.crudposts.infra.entities.PostEntity;
import com.example.crudposts.infra.mappers.PostPersistenceMapper;
import com.example.crudposts.infra.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostPersistenceMapper postMapper;

    @Transactional
    public Post create(Post postDomain) {
        var entity = postMapper.toEntity(postDomain);
        entity.setArchived(false);

        var savedEntity = postRepository.save(entity);
        return postMapper.toDomain(savedEntity);
    }

    @Transactional(readOnly = true)
    public Post findById(UUID id) {
        return postRepository.findById(id)
                .map(postMapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Post não encontrado."));
    }

    @Transactional
    public Post update(UUID id, Post postUpdate) {
        PostEntity entity = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post não encontrado para edição."));

        entity.setText(postUpdate.text());
        return postMapper.toDomain(postRepository.save(entity));
    }

    @Transactional
    public void delete(UUID id) {
        if (!postRepository.existsById(id)) throw new RuntimeException("Post inexistente.");
        postRepository.deleteById(id);
    }

    @Transactional
    public void archive(UUID id) {
        PostEntity entity = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post não encontrado para arquivamento."));

        entity.setArchived(true);
        postRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<Post> findPublicPostsByUserId(UUID userId) {
        return postRepository.findByUserIdAndArchivedFalse(userId)
                .stream()
                .map(postMapper::toDomain)
                .toList();
    }
}
