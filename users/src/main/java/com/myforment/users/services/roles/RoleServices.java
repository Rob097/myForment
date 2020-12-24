package com.myforment.users.services.roles;

import java.util.ArrayList;
import java.util.Optional;

import com.myforment.users.models.Role;
import com.myforment.users.models.enums.ERole;

public interface RoleServices {

	public ArrayList<Role> getAll() throws Exception;
	
	public Role getByName(String name) throws Exception;
	
	Optional<Role> findByName(ERole name);
	
}
