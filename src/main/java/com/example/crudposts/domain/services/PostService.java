package com.example.crudposts.domain.services;

import com.example.crudposts.domain.models.Post;
import com.example.crudposts.exceptions.custom.EntityNotFoundException;
import com.example.crudposts.domain.entities.PostEntity;
import com.example.crudposts.domain.mappers.PostPersistenceMapper;
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
    private final UserService userService;

    @Transactional
    public Post create(Post postDomain) {
        userService.findById(postDomain.userId());

        var entity = postMapper.toEntity(postDomain);
        entity.setArchived(false);

        var savedEntity = postRepository.save(entity);
        return postMapper.toDomain(savedEntity);
    }

    @Transactional(readOnly = true)
    public Post findById(UUID id) {
        return postRepository.findById(id)
                .map(postMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Post não encontrado."));
    }

    @Transactional
    public Post update(UUID id, Post postUpdate) {
        PostEntity entity = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post não encontrado para edição."));

        entity.setText(postUpdate.text());
        return postMapper.toDomain(postRepository.save(entity));
    }

    @Transactional
    public void delete(UUID id) {
        if (!postRepository.existsById(id)) {
            throw new EntityNotFoundException("Post inexistente.");
        }
        postRepository.deleteById(id);
    }

    @Transactional
    public void archive(UUID id) {
        PostEntity entity = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post não encontrado para arquivamento."));

        entity.setArchived(true);
        postRepository.save(entity);
    }

    @Transactional
    public void unarchive(UUID id) {
        PostEntity entity = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post não encontrado para desarquivamento."));

        entity.setArchived(false); // Volta a ser público
        postRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<Post> findPublicPostsByUserId(UUID userId) {
        userService.findById(userId);

        return postRepository.findByUserIdAndArchivedFalse(userId)
                .stream()
                .map(postMapper::toDomain)
                .toList();
    }
}