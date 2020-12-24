package com.myforment.users.repository;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;
import com.myforment.users.models.Contract;
import com.myforment.users.models.Role;
import com.myforment.users.models.User;
import com.myforment.users.models.Utente;
import com.myforment.users.multitenant.MongoTemplateCustom;

@Repository(value="customUsers")
public class UserRepositoryCustomImpl implements UserRepositoryCustom{

	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;
	
	@Autowired
	@Qualifier("utentiTemplate")
	private MongoTemplateCustom utentiTemplate;
	
	
	
	
	/* ############################   GENERAL ############################ */
	
	@Override
	public void setDefaultUserDb(String userId) {
		utentiTemplate.setDefaultUserDb(userId);
	}

	/* ############################   USERS AND UTENTI ############################ */
	
	@Override
	public User findUserModelByUsername(String username) {
		Query query = new Query().addCriteria(Criteria.where("username").is(username));
		return mongoTemplate.findOne(query, User.class);
	}
	
	@Override
	public User findUserModelByEmail(String email) {
		Query query = new Query().addCriteria(Criteria.where("email").is(email));
		return mongoTemplate.findOne(query, User.class);
	}


	@Override
	public Utente findUtenteModelById(String id, boolean isCurrent) {
		if(isCurrent)
			return utentiTemplate.findById(id, Utente.class);
		else
			return utentiTemplate.setDatabaseName(id).findById(id, Utente.class);
	}
	
	
	@Override
	public Utente getCurrentUtenteModel() throws UsernameNotFoundException{
		return this.findUtenteModelById(utentiTemplate.getMultitenantMongoDbFactory().getDefaultId(), true);
	}


	@Override
	public ArrayList<User> findAllUsers() {		
		return new ArrayList<>(utentiTemplate.findAll(User.class));
	}
	
	@Override
	public Boolean existsByUsername(String username) {
		if(findUserModelByUsername(username) != null)
			return true;
		return false;
	}
	
	@Override
	public Boolean existsByEmail(String email) {
		if(findUserModelByEmail(email) != null)
			return true;
		return false;
	}

	@Override
	public Utente saveUtente(Utente u) throws Exception {
		return utentiTemplate.save(u);
	}
	
	@Override
	public User saveUser(User u) throws Exception{
		return mongoTemplate.save(u);
	}
	
	
	
	/* ############################   CONTRACTS  ############################ */
	
	@Override
	public Contract addContract(Contract c, String userId) throws Exception {
		if(userId == null)
			return utentiTemplate.save(c);
		else
			return utentiTemplate.setDatabaseName(userId).save(c);
	}
	
	@Override
	public ArrayList<Contract> getAllContracts(String userId) throws Exception {
		if(userId == null)
			return (ArrayList<Contract>) utentiTemplate.findAll(Contract.class);
		else
			return (ArrayList<Contract>) utentiTemplate.setDatabaseName(userId).findAll(Contract.class);
	}

	@Override
	public DeleteResult removeContract(Contract c, String userId) throws Exception {
		if(userId == null)
			return utentiTemplate.remove(c);
		else
			return utentiTemplate.setDatabaseName(userId).remove(c);
	}


	
	/* ############################   ROLES ############################ */
	
	@Override
	public ArrayList<Role> getAllRoles() throws Exception {
		return (ArrayList<Role>) mongoTemplate.findAll(Role.class);
	}


	@Override
	public Role getRoleByName(String name) {
		Query query = new Query().addCriteria(Criteria.where("name").is(name));
		return (Role) mongoTemplate.find(query, Role.class);
	}

}
