package com.myforment.users.services;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.myforment.users.models.Role;

public class RoleServicesImpl implements RoleServices {

	@Autowired
	@Qualifier("mongoTemplate")
	MongoTemplate mongoTemplate;
	
	@Autowired
	HttpServletRequest request;
	
	@Override
	public ArrayList<Role> getAll() {
		ArrayList<Role> roles = new ArrayList<>();
		roles.addAll(mongoTemplate.findAll(Role.class));
		return roles;
	}

}
