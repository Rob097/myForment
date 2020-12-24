package com.myforment.companies.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoCollection;
import com.myforment.companies.models.Company;
import com.myforment.companies.models.Employee;
import com.myforment.companies.models.Permission;
import com.myforment.companies.models.Role;
import com.myforment.companies.models.enums.ERole;
import com.myforment.users.encryption.EncryptionUtil;
import com.myforment.users.models.User;
import com.myforment.users.models.Utente;
import com.myforment.users.multitenant.MongoTemplateCustom;


@Repository(value="customCompanies")
public class CompanyRepositoryCustomImpl implements CompanyRepositoryCustom {

	@Autowired
	@Qualifier("companyTemplate")
	MongoTemplateCustom companyTemplate;

	@Autowired
	@Qualifier("mongoTemplate")
	MongoTemplate mongoTemplate;

	@Autowired
	EncryptionUtil encryptionUtil;

	
	
	
	
	/*  ################################# COMPANIES #################################  */
	
	
	// ========================================================================================
	
	@Override
	public ArrayList<Company> getAllCompanies() throws Exception {
		return new ArrayList<Company>(mongoTemplate.findAll(Company.class));
	}
	
	// ========================================================================================

	@Override
	public Company getCompanyById(String id) throws Exception {
		return mongoTemplate.findById(id, Company.class);
	}
	
	// ========================================================================================

	@Override
	public boolean existCompanyByName(String name) {
		try {
			Query query = new Query().addCriteria(Criteria.where("name").is(encryptionUtil.encrypt(name)));
			return mongoTemplate.exists(query, Company.class);
		} catch (Exception e) {
			return false;
		}
	}
	
	// ========================================================================================

	@Override
	public boolean existCompanyByLegalName(String legalName) throws Exception {
		if (legalName == null || legalName.isEmpty())
			return false;
		else {
			Query query = new Query().addCriteria(Criteria.where("legalName").is(encryptionUtil.encrypt(legalName)));
			return mongoTemplate.exists(query, Company.class);
		}
	}
	
	// ========================================================================================

	@Override
	public Company saveCompany(Company company) throws Exception {
		return mongoTemplate.save(company);
	}
	
	// ========================================================================================

	@Override
	public void removecompany(String companyId) throws Exception {
		Query query = new Query().addCriteria(Criteria.where("_id").is(companyId));
		mongoTemplate.remove(query, Company.class);

		for (String collectionName : companyTemplate.setDatabaseName(companyId).getCollectionNames()) {
			MongoCollection<Document> collection = companyTemplate.setDatabaseName(companyId).getCollection(collectionName);
			collection.drop();
		}

	}
	
	// ========================================================================================
	
	

	
	/*  ################################# EMPLOYEES #################################  */
	
	
	// ========================================================================================
	
	@Override
	public ArrayList<Employee> getAllEmployees(String companyId) throws Exception {
		return (ArrayList<Employee>) companyTemplate.setDatabaseName(companyId).findAll(Employee.class);
	}
	
	// ========================================================================================

	@Override
	public ArrayList<String> getAllEmployeesId(String companyId) throws Exception {
		ArrayList<Employee> employees = getAllEmployees(companyId);

		return (ArrayList<String>) employees.stream().map(o -> o.getUserId()).collect(Collectors.toList());
	}
	
	// ========================================================================================

	@Override
	public ArrayList<User> loadAllUsersExceptEmployees(String companyId) throws Exception {
		ArrayList<String> ids = getAllEmployeesId(companyId);

		Query query = new Query().addCriteria(Criteria.where("_id").nin(ids));
		
		return (ArrayList<User>) mongoTemplate.find(query, User.class);
	}
	
	// ========================================================================================

	@Override
	public Employee addEmployee(Utente employee, String companyId, ERole role) throws Exception {
		Query query = new Query().addCriteria(Criteria.where("roleName").is(role.toString()));

		List<Role> roles = new ArrayList<>(companyTemplate.setDatabaseName(companyId).find(query, Role.class));

		Employee e = new Employee(employee.getId(), roles);

		return companyTemplate.setDatabaseName(companyId).save(e);
	}
	
	// ========================================================================================

	@Override
	public void removeEmployeeByUserId(String userId, String companyId) throws Exception {
		Query query = new Query().addCriteria(Criteria.where("userId").is(userId));
		companyTemplate.setDatabaseName(companyId).remove(query, Employee.class);
	}
	
	// ========================================================================================
	
	
	
	
	/* ################################# ROLES ################################# */

	
	// ========================================================================================
	
	@Override
	public ArrayList<Permission> getAllPermissions() throws Exception {
		return new ArrayList<Permission>(mongoTemplate.findAll(Permission.class));
	}
	
	// ========================================================================================

	@Override
	public Role getRoleById(String roleId, String companyId) throws Exception {
		return companyTemplate.setDatabaseName(companyId).findById(roleId, Role.class);
	}
	
	// ========================================================================================

	@Override
	public Role getRoleByName(ERole name, String companyId) throws Exception {
		Query query = new Query().addCriteria(Criteria.where("roleName").is(name.toString()));
		return companyTemplate.setDatabaseName(companyId).findOne(query, Role.class);
	}
	
	// ========================================================================================

	@Override
	public Role addNewRoleToCompany(Role role, String companyId) throws Exception {
		return companyTemplate.setDatabaseName(companyId).save(role);
	}
	
	// ========================================================================================

}
