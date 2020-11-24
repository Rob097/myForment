package com.myforment.users.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myforment.users.models.ERole;
import com.myforment.users.models.Role;

/**
 * @author Roberto97
 * Link to Database to the table of Roles.
 */
public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
