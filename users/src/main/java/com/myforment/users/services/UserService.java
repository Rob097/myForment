package com.myforment.users.services;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mongodb.client.result.DeleteResult;
import com.myforment.users.models.Contract;
import com.myforment.users.models.User;
import com.myforment.users.models.Utente;

public interface UserService {

	public void setDefaultUserDb(String userId) throws Exception;
	
	public User getUserModelByUsername(String username) throws UsernameNotFoundException;
	
	public Utente getUtenteModelById(String id) throws UsernameNotFoundException;
	
	public Utente getCurrentUtenteModel() throws UsernameNotFoundException;
	
	public ArrayList<User> loadAllUsers();
	
	public Boolean existsByUsername(String username);
	
	public Boolean existsByEmail(String email);
	
	public Utente saveUtente(Utente u) throws Exception;
	
	public User saveUser(User u) throws Exception;
	
	public Contract addContract(Contract c, String... userId) throws Exception;
	
	public ArrayList<Contract> getAllContracts(String... userId) throws Exception; //String userId
	
	public DeleteResult removeContract(Contract c, String userId) throws Exception;
	
}
