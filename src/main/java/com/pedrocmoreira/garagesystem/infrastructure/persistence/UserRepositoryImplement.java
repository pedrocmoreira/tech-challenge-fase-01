package com.pedrocmoreira.garagesystem.infrastructure.persistence;

import com.pedrocmoreira.garagesystem.domain.model.User;
import com.pedrocmoreira.garagesystem.domain.repository.UserRepository;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImplement implements UserRepository {
    private final UserJpaRepository jpa;

    @Override
    public void save(User user) {
        jpa.save(UserMapper.toEntity(user));
    }

    @Override
    public Optional<User> filterById(Long id) {
        return jpa.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> filterByUsername(String username) {
        return jpa.findByUsername(username).map(UserMapper::toDomain);
    }

    @Override
    public List<User> listAll() {
        return jpa.findAll().stream().map(UserMapper::toDomain).toList();
    }

    @Override
    public void delete(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpa.existsByUsername(username);
    }
}