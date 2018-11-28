package com.conoftherings.dao;

import org.springframework.data.repository.CrudRepository;

import com.conoftherings.domain.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}
