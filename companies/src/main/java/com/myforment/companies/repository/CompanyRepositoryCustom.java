package com.myforment.companies.repository;

import java.util.ArrayList;

import com.myforment.companies.models.Company;
import com.myforment.companies.models.Employee;
import com.myforment.companies.models.Permission;
import com.myforment.companies.models.Role;
import com.myforment.companies.models.enums.ERole;
import com.myforment.users.models.User;
import com.myforment.users.models.Utente;

public interface CompanyRepositoryCustom {

	/* ########### COMPANIES ########### */

	/* GET */

	ArrayList<Company> getAllCompanies() throws Exception;

	Company getCompanyById(String id) throws Exception;

	boolean existCompanyByName(String name);

	boolean existCompanyByLegalName(String legalName) throws Exception;

	/* ADD */

	Company saveCompany(Company company) throws Exception;

	/* REMOVE */

	void removecompany(String companyId) throws Exception;
	
	
	

	/* ########### EMPLOYEES ########### */

	/* GET */

	ArrayList<Employee> getAllEmployees(String companyId) throws Exception;

	ArrayList<String> getAllEmployeesId(String companyId) throws Exception;

	ArrayList<User> loadAllUsersExceptEmployees(String companyId) throws Exception;

	/* ADD */

	Employee addEmployee(Utente employee, String companyId, ERole role) throws Exception;

	/* REMOVE */

	void removeEmployeeByUserId(String employeeId, String companyId) throws Exception;
	
	
	

	/* ########### ROLES ########### */

	/* GET */

	ArrayList<Permission> getAllPermissions() throws Exception;

	Role getRoleById(String roleId, String companyId) throws Exception;

	Role getRoleByName(ERole name, String companyId) throws Exception;

	/* ADD */

	Role addNewRoleToCompany(Role role, String companyId) throws Exception;

}
