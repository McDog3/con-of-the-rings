package com.conoftherings.dao;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.conoftherings.domain.User;

public interface UserRepository extends Repository<User, Integer> {

    Iterable<User> findAll();

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);

    User save(User user);
}
