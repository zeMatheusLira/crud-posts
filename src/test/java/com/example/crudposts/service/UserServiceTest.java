package com.example.crudposts.service;

import com.example.crudposts.domain.entities.UserEntity;
import com.example.crudposts.domain.mappers.UserPersistenceMapper;
import com.example.crudposts.domain.models.User;
import com.example.crudposts.domain.services.UserService;
import com.example.crudposts.exceptions.custom.BusinessException;
import com.example.crudposts.exceptions.custom.EntityNotFoundException;
import com.example.crudposts.infra.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPersistenceMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User userDomain;
    private UserEntity userEntity;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        userDomain = new User(userId, "zematheus", "Jose", "ze@test.com", "123", "Bio", null, null);
        userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail("ze@test.com");
    }

    @Test
    void create_ShouldReturnSavedUser_WhenDataIsUnique() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userMapper.toEntity(any())).thenReturn(userEntity);
        when(userRepository.save(any())).thenReturn(userEntity);
        when(userMapper.toDomain(any())).thenReturn(userDomain);

        User result = userService.create(userDomain);

        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(userDomain.email());
        verify(userRepository).save(any());
    }

    @Test
    void create_ShouldThrowBusinessException_WhenEmailExists() {
        when(userRepository.existsByEmail(userDomain.email())).thenReturn(true);

        assertThatThrownBy(() -> userService.create(userDomain))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("E-mail já cadastrado");

        verify(userRepository, never()).save(any());
    }

    @Test
    void findById_ShouldReturnUser_WhenIdExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDomain(userEntity)).thenReturn(userDomain);

        User result = userService.findById(userId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(userId);
    }

    @Test
    void findById_ShouldThrowEntityNotFoundException_WhenIdDoesNotExist() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(userId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void delete_ShouldCallRepositoryDelete_WhenUserExists() {
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.delete(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void delete_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(() -> userService.delete(userId))
                .isInstanceOf(EntityNotFoundException.class);

        verify(userRepository, never()).deleteById(any());
    }
}
