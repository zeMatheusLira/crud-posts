package com.example.crudposts.domain.services;

import com.example.crudposts.domain.models.User;
import com.example.crudposts.exceptions.custom.BusinessException;
import com.example.crudposts.exceptions.custom.EntityNotFoundException;
import com.example.crudposts.domain.mappers.UserPersistenceMapper;
import com.example.crudposts.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserPersistenceMapper userMapper;

    @Transactional
    public User create(User userDomain) {
        if (userRepository.existsByEmail(userDomain.email())) {
            throw new BusinessException("Erro de Integridade: E-mail já cadastrado.");
        }
        if (userRepository.existsByUsername(userDomain.username())) {
            throw new BusinessException("Erro de Integridade: Nome de usuário já está em uso.");
        }

        var entity = userMapper.toEntity(userDomain);
        var savedEntity = userRepository.save(entity);

        return userMapper.toDomain(savedEntity);
    }

    @Transactional(readOnly = true)
    public User findById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID"));
    }

    @Transactional
    public User update(UUID id, User userUpdate) {
        var existingEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado para atualização."));

        existingEntity.setName(userUpdate.name());
        existingEntity.setBiography(userUpdate.biography());

        var updatedEntity = userRepository.save(existingEntity);
        return userMapper.toDomain(updatedEntity);
    }

    @Transactional
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Falha ao deletar: Usuário inexistente.");
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDomain)
                .toList();
    }
}