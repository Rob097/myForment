package com.myforment.users.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mongodb.client.result.DeleteResult;
import com.myforment.users.models.Contract;
import com.myforment.users.models.User;
import com.myforment.users.models.Utente;
import com.myforment.users.repository.UserRepositoryCustom;

/**
 * @author Roberto97
 * The UserDetailsService is a core interface in Spring Security framework, which is used to retrieve the user’s authentication and authorization information. 
 */
@Service(value="currentUsersService")
public class UserServiceImpl implements UserService {

	
	@Autowired
	@Qualifier("customUsers") //Annotation used to define which interface implementation to use
	private UserRepositoryCustom userRepository;	
	
	
	
	
	
	@Override
	public void setDefaultUserDb(String userId) throws Exception{
		userRepository.setDefaultUserDb(userId);
	}
	
	//============================================================================================================================
	
	@Override
	public User getUserModelByUsername(String username) throws UsernameNotFoundException {
		
		return userRepository.findUserModelByUsername(username);		
		
	}
	
	//============================================================================================================================
	
	@Override
	public Utente getUtenteModelById(String id) throws UsernameNotFoundException {
		
		return userRepository.findUtenteModelById(id, true);
		
	}
	
	@Override
	public Utente getCurrentUtenteModel() throws UsernameNotFoundException{
		return userRepository.getCurrentUtenteModel();
	}

	//============================================================================================================================
	
	@Override
	public ArrayList<User> loadAllUsers(){
		return (ArrayList<User>) userRepository.findAllUsers();
	}
	
	//============================================================================================================================	
	
	@Override
	public Boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	//============================================================================================================================
	
	@Override
	public Boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	//============================================================================================================================
	
	@Override
	public Utente saveUtente(Utente u) throws Exception {
		return userRepository.saveUtente(u);		
	}
	
	@Override
	public User saveUser(User u) throws Exception{
		return userRepository.saveUser(u);	
	}

	//============================================================================================================================
	
	@Override
	public Contract addContract(Contract c, String... userId) throws Exception{
		return userRepository.addContract(c, null);	
	}
	
	//============================================================================================================================
	
	@Override
	public ArrayList<Contract> getAllContracts(String... userId) throws Exception{
		return userRepository.getAllContracts(null);
	}
	
	//============================================================================================================================
	
	@Override
	public DeleteResult removeContract(Contract c, String userId) throws Exception{
		return userRepository.removeContract(c, null);
	}
	
	//============================================================================================================================
	
}
