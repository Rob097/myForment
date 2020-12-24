package com.myforment.users.services.roles;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.myforment.users.models.Role;
import com.myforment.users.models.enums.ERole;
import com.myforment.users.repository.UserRepositoryCustom;

@Service(value="RoleServicesImpl")
public class RoleServicesImpl implements RoleServices {

	@Autowired
	@Qualifier("customUsers") //Annotation used to define which interface implementation to use
	private UserRepositoryCustom userRepository;
	
	@Override
	public ArrayList<Role> getAll() throws Exception {
		return userRepository.getAllRoles();
	}

	@Override
	public Role getByName(String name) throws Exception {
		return userRepository.getRoleByName(name);
	}

	@Override
	public Optional<Role> findByName(ERole name) {
		try {
			return Optional.ofNullable(userRepository.getRoleByName(name.toString()));
		} catch (Exception e) {return null;}
	}

}
