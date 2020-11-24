package com.myforment.users.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myforment.users.models.User;
import com.myforment.users.models.Utente;
import com.myforment.users.repository.UserRepository;

/**
 * @author Roberto97
 * The UserDetailsService is a core interface in Spring Security framework, which is used to retrieve the user’s authentication and authorization information. 
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;
	
	@Autowired
	@Qualifier("utentiTemplate")
	private MongoTemplate utentiTemplate;

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
		return (ArrayList<User>) mongoTemplate.findAll(User.class);
	}
	
	public Utente save(Utente u) {
		System.out.println("SALVA");
		try {
			utentiTemplate.save(u);
			return u;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
