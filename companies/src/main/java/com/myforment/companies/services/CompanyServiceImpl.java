package com.myforment.companies.services;

import java.util.ArrayList;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.myforment.companies.models.Company;
import com.myforment.companies.models.Employee;
import com.myforment.companies.models.Permission;
import com.myforment.companies.models.Role;
import com.myforment.companies.models.enums.ERole;
import com.myforment.companies.repository.CompanyRepositoryCustom;
import com.myforment.users.models.User;
import com.myforment.users.models.Utente;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	@Qualifier("customCompanies") //Annotation used to define which interface implementation to use
	private CompanyRepositoryCustom companyRepository;

	
	
	
	
	
	
	/*
	 * ################################# COMPANIES #################################
	 */
	
	
	

	/* --------------------------------- GET --------------------------------- */

	// ========================================================================================

	@Override
	public ArrayList<Company> getAllCompanies() throws Exception {
		return companyRepository.getAllCompanies();
	}

	// ========================================================================================

	@Override
	public Company getCompanyById(String id) throws Exception {
		return companyRepository.getCompanyById(id);
	}

	// ========================================================================================

	/*
	 * Methods to check if the company already exists checking different parameters
	 */
	@Override
	public boolean existCompanyByName(@NotBlank String name) {
		
		return companyRepository.existCompanyByName(name);
		
	}

	// ========================================================================================

	@Override
	public boolean existCompanyByLegalName(String legalName) throws Exception {

		return companyRepository.existCompanyByLegalName(legalName);
		
	}

	// ========================================================================================
	
	

	/* --------------------------------- ADD --------------------------------- */

	// ========================================================================================

	// Save copany into general database
	@Override
	public Company saveCompany(Company company) throws Exception {
		return companyRepository.saveCompany(company);
	}

	// ========================================================================================
	
	

	/* --------------------------------- REMOVE --------------------------------- */

	// ========================================================================================

	@Override
	public void removecompany(String companyId) throws Exception {

		companyRepository.removecompany(companyId);

	}

	// ========================================================================================
	
	
	
	
	
	

	/*
	 * ################################# EMPLOYEES #################################
	 */
	
	

	/* --------------------------------- GET --------------------------------- */

	// ========================================================================================

	@Override
	public ArrayList<Employee> getAllEmployees(String companyId) throws Exception {
		return companyRepository.getAllEmployees(companyId);
	}

	// ========================================================================================

	@Override
	public ArrayList<String> getAllEmployeesId(String companyId) throws Exception {
		return companyRepository.getAllEmployeesId(companyId);
	}

	// ========================================================================================

	@Override
	public ArrayList<User> loadAllUsersExceptEmployees(String companyId) throws Exception {
		return companyRepository.loadAllUsersExceptEmployees(companyId);
	}

	// ========================================================================================
	
	

	/* --------------------------------- ADD --------------------------------- */

	// ========================================================================================

	// Add employee to a company
	@Override
	public Employee addEmployee(Utente employee, String companyId, ERole role) throws Exception {
		return companyRepository.addEmployee(employee, companyId, role);
	}

	// ========================================================================================
	
	

	/* --------------------------------- REMOVE --------------------------------- */

	// ========================================================================================

	@Override
	public void removeEmployeeByUserId(String userId, String companyId) throws Exception {

		companyRepository.removeEmployeeByUserId(userId, companyId);

	}

	// ========================================================================================
	
	
	
	
	
	

	/* ################################# ROLES ################################# */
	
	

	/* --------------------------------- GET --------------------------------- */

	// ========================================================================================

	@Override
	public ArrayList<Permission> getAllPermissions() throws Exception {
		return companyRepository.getAllPermissions();
	}

	// ========================================================================================

	@Override
	public Role getRoleById(String roleId, String companyId) throws Exception {

		return companyRepository.getRoleById(roleId, companyId);

	}

	// ========================================================================================

	@Override
	public Role getRoleByName(ERole name, String companyId) throws Exception {
		
		return companyRepository.getRoleByName(name, companyId);

	}

	// ========================================================================================
	
	

	/* --------------------------------- GET --------------------------------- */	

	@Override
	public Role addNewRoleToCompany(Role role, String companyId) throws Exception {
		
		return companyRepository.addNewRoleToCompany(role, companyId);

	}

	// ========================================================================================

}
