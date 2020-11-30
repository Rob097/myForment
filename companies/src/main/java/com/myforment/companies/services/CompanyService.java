package com.myforment.companies.services;

import java.util.ArrayList;

import com.myforment.companies.models.Company;
import com.myforment.companies.models.Employee;
import com.myforment.companies.models.Permission;
import com.myforment.companies.models.Role;
import com.myforment.users.models.Utente;

public interface CompanyService {

	public ArrayList<Company> GetAll();
	
	public ArrayList<Permission> getAllPermissions();
	
	public Company GetById(String id);
	
	public Company GeneralSave(Company company);
	
	public Employee addEmployee(Utente employee, Company company);
	
	public Role addRole(Role role, Company company);
	
	public boolean existByName(String name);
	
	public boolean existByLegalName(String name);
	
}
