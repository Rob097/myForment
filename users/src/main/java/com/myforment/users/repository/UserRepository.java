package com.myforment.users.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myforment.users.models.User;

/**
 * @author Roberto97
 * Link to Database to the table of Users.
 */
public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}
