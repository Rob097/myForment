package com.multitenant.jwt.users.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multitenant.jwt.users.repository.UserRepository;
import com.multitenant.jwt.users.models.User;

/**
 * @author Roberto97
 * The UserDetailsService is a core interface in Spring Security framework, which is used to retrieve the user’s authentication and authorization information. 
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	@Qualifier("generalTemplate")
	private MongoTemplate generalTemplate;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

		return UserDetailsImpl.build(user);
	}
	
	public User loadUserModelByUsername(String username) throws UsernameNotFoundException {
		
		return userRepository.findByUsername(username).orElse(null);		
		
	}
	
	public ArrayList<User> loadAllUsers(){
		return (ArrayList<User>) generalTemplate.findAll(User.class);
	}

}
