package com.pedrocmoreira.garagesystem.infrastructure.persistence;

import com.pedrocmoreira.garagesystem.domain.model.User;
import com.pedrocmoreira.garagesystem.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository jpa;

    @Override public User save(User user) {
        return jpa.save(user);
    }

    @Override
    public Optional<User> filterById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public Optional<User> filterByUsername(String username) {
        return jpa.findByUsername(username);
    }

    @Override
    public List<User> listAll() {
        return jpa.findAll();
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
