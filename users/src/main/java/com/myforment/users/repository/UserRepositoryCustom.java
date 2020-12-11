package com.myforment.users.repository;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mongodb.client.result.DeleteResult;
import com.myforment.users.models.Contract;
import com.myforment.users.models.Role;
import com.myforment.users.models.User;
import com.myforment.users.models.Utente;

public interface UserRepositoryCustom {
	
	/* GENERAL */
	
	void setDefaultUserDb(String userId);
	
	/* USERS AND UTENTI */

	User findUserModelByUsername(String username);
	
	User findUserModelByEmail(String email);
	
	Utente findUtenteModelById(String userId, boolean isCurrent);
	
	Utente getCurrentUtenteModel() throws UsernameNotFoundException;
	
	ArrayList<User> findAllUsers();
	
	Boolean existsByUsername(String username);
	
	Boolean existsByEmail(String email);
	
	Utente saveUtente(Utente u) throws Exception;
	
	User saveUser(User u) throws Exception;
	
	
	
	/* CONTRACTS */
	
	Contract addContract(Contract c, String userId) throws Exception;
	
	ArrayList<Contract> getAllContracts(String userId) throws Exception;
	 
	DeleteResult removeContract(Contract c, String userId) throws Exception;
	
	
	
	/* ROLES */
	
	ArrayList<Role> getAllRoles() throws Exception;
	
	Role getRoleByName(String name);
	
}
