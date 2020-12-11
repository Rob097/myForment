package com.myforment.companies.services;

import java.util.ArrayList;

import com.myforment.companies.models.Company;
import com.myforment.companies.models.Employee;
import com.myforment.companies.models.Permission;
import com.myforment.companies.models.Role;
import com.myforment.companies.models.enums.ERole;
import com.myforment.users.models.User;
import com.myforment.users.models.Utente;

public interface CompanyService {
	
	/* ###########  COMPANIES  ########### */
	
	/* GET */

	public ArrayList<Company> getAllCompanies() throws Exception;	
	
	public Company getCompanyById(String id) throws Exception;
	
	public boolean existCompanyByName(String name) throws Exception;
	
	public boolean existCompanyByLegalName(String legalName) throws Exception;
	
	/* ADD */
	
	public Company saveCompany(Company company) throws Exception;
	
	/* REMOVE */
	
	public void removecompany(String companyId) throws Exception;
	
	
	
	
	
	
	/* ###########  EMPLOYEES  ########### */
	
	/* GET */
	
	public ArrayList<Employee> getAllEmployees(String companyId) throws Exception;
	
	public ArrayList<String> getAllEmployeesId(String companyId) throws Exception;
	
	public ArrayList<User> loadAllUsersExceptEmployees(String companyId) throws Exception;
	
	/* ADD */
	
	public Employee addEmployee(Utente employee, String companyId, ERole role) throws Exception;
	
	/* REMOVE */
	
	public void removeEmployeeByUserId(String employeeId, String companyId) throws Exception;
	
	
	
	
	
	
	/* ROLES */
	
	/* GET */
	
	public ArrayList<Permission> getAllPermissions() throws Exception;
	
	public Role getRoleById(String roleId, String companyId) throws Exception;

	public Role getRoleByName(ERole name, String companyId) throws Exception;
	
	/* ADD */
	
	public Role addNewRoleToCompany(Role role, String companyId) throws Exception;

	
	
}
