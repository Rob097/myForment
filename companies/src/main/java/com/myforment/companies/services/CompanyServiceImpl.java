package com.myforment.companies.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.myforment.companies.models.Company;
import com.myforment.companies.models.Employee;
import com.myforment.companies.models.Permission;
import com.myforment.companies.models.Role;
import com.myforment.users.models.Utente;
import com.myforment.users.multitenant.MongoTemplateCustom;
import com.myforment.companies.models.enums.ERole;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	@Qualifier("companyTemplate")
	MongoTemplateCustom companyTemplate;

	@Autowired
	@Qualifier("mongoTemplate")
	MongoTemplate mongoTemplate;

	@Autowired
	HttpServletRequest request;
	
	
	
	//========================================================================================

	@Override
	public ArrayList<Company> GetAll() {
		return new ArrayList<Company>(mongoTemplate.findAll(Company.class));
	}
	
	//========================================================================================
	
	@Override
	public ArrayList<Permission> getAllPermissions() {
		return new ArrayList<Permission>(mongoTemplate.findAll(Permission.class));
	}
	
	//========================================================================================

	@Override
	public Company GetById(String id) {
		Company c = mongoTemplate.findById(id, Company.class);
		return c;
	}
	
	//========================================================================================

	//Save copany into general database
	@Override
	public Company GeneralSave(Company company) {
		mongoTemplate.save(company);
		return company;
	}
	
	//========================================================================================

	//Add employee to a company
	@Override
	public Employee addEmployee(Utente employee, Company company) {
		List<Role> roles = new ArrayList<>();
		Query query = new Query();
		List<Role> r = new ArrayList<>();
		query.addCriteria(Criteria.where("name").is(ERole.ROLE_OWNER));

		r = companyTemplate.setDatabaseName(company.getIdCom().toString()).find(query, Role.class);
		
		roles.addAll(r);
		Employee e = new Employee(employee.getId(), roles);

		return companyTemplate.setDatabaseName(company.getIdCom().toString()).save(e);
	}
	
	//========================================================================================
	
	@Override
	public Role addRole(Role role, Company company) {

		companyTemplate.setDatabaseName(company.getIdCom().toString()).save(role);
		
		return null;
	}
	
	//========================================================================================
	
	@Override
	public Role getRoleById(String id, String companyId) {
		Role r = companyTemplate.setDatabaseName(companyId).findById(id, Role.class);
		return r;
		
	}
	
	//========================================================================================

	/*
	 * Methods to check if the company already exists checking different parameters
	 */
	@Override
	public boolean existByName(String name) {
		if (name != null && !name.isEmpty()) {
			Query query = new Query();
			query.addCriteria(Criteria.where("name").is(name));
			boolean b = mongoTemplate.exists(query, Company.class);
			return b;
		}
		return false;
	}
	
	//========================================================================================

	@Override
	public boolean existByLegalName(String legalName) {
		if (legalName != null && !legalName.isEmpty()) {
			Query query = new Query();
			query.addCriteria(Criteria.where("legalName").is(legalName));
			boolean b = mongoTemplate.exists(query, Company.class);
			return b;
		}
		return false;
	}
	
	//========================================================================================

}
