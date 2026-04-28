package com.pedrocmoreira.garagesystem.domain.repository;

import com.pedrocmoreira.garagesystem.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> filterById(Long id);
    Optional<User> filterByUsername(String username);
    List<User> listAll();
    void delete(Long id);
    boolean existsByUsername(String username);
}
