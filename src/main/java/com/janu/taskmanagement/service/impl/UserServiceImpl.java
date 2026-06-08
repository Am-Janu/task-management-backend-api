package com.janu.taskmanagement.service.impl;

import com.janu.taskmanagement.dto.UserDto;
import com.janu.taskmanagement.entity.User;
import com.janu.taskmanagement.exception.DuplicateResourceException;
import com.janu.taskmanagement.exception.ResourceNotFoundException;
import com.janu.taskmanagement.repository.UserRepository;
import com.janu.taskmanagement.service.UserService;
import com.janu.taskmanagement.repository.UserRepository;
import com.janu.taskmanagement.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new DuplicateResourceException("Username '" + userDto.getUsername() + "' is already taken.");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new DuplicateResourceException("Email '" + userDto.getEmail() + "' is already in use.");
        }

        User user = mapToEntity(userDto);
        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return mapToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return mapToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // Mapper Methods
    private User mapToEntity(UserDto dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword()) // In production, hash password before saving
                .email(dto.getEmail())
                .role(dto.getRole() != null ? dto.getRole() : "ROLE_USER")
                .build();
    }

    private UserDto mapToDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .role(entity.getRole())
                .build();
    }
}
