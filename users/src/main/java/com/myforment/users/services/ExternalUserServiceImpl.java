package com.myforment.users.services;

import java.util.ArrayList;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mongodb.client.result.DeleteResult;
import com.myforment.users.models.Contract;
import com.myforment.users.models.User;
import com.myforment.users.models.Utente;
import com.myforment.users.repository.UserRepositoryCustom;

@Service(value="externalUsersService")
public class ExternalUserServiceImpl implements UserService {

	@Autowired
	@Qualifier("customUsers") //Annotation used to define which interface implementation to use
	private UserRepositoryCustom userRepository;
	
	
	
	
	@Override
	public Utente getUtenteModelById(String id) throws UsernameNotFoundException {
		return userRepository.findUtenteModelById(id, false);
	}

	@Override
	public Contract addContract(Contract c, @NotBlank String... userId) throws Exception {
		return userRepository.addContract(c, userId[0]);
	}

	@Override
	public ArrayList<Contract> getAllContracts(@NotBlank String... userId) throws Exception {
		return userRepository.getAllContracts(userId[0]);
	}

	@Override
	public DeleteResult removeContract(Contract c, String userId) throws Exception {
		return userRepository.removeContract(c, userId);
	}

	@Override
	public User getUserModelByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findUserModelByUsername(username);
	}

	@Override
	public ArrayList<User> loadAllUsers() {
		return (ArrayList<User>) userRepository.findAllUsers();
	}

	@Override
	public Utente saveUtente(Utente u) throws Exception {
		return userRepository.saveUtente(u);
	}

	
	
	
	
	
	/* METHODS NOT NEEDED FOR EXTERNAL USERS THAT ARE IMPLEMENTED IN CURRENT USER */
	@Override
	public Utente getCurrentUtenteModel() throws UsernameNotFoundException {
		return null;
	}

	@Override
	public Boolean existsByUsername(String username) {
		return null;
	}

	@Override
	public Boolean existsByEmail(String email) {
		return null;
	}

	@Override
	public User saveUser(User u) throws Exception {
		return null;
	}

	@Override
	public void setDefaultUserDb(String userId) throws Exception {}

}
