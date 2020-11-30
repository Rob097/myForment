package com.myforment.users.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myforment.users.models.Role;
import com.myforment.users.models.enums.ERole;

/**
 * @author Roberto97
 * Link to Database to the table of Roles.
 */
public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
