package com.myforment.companies.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myforment.companies.models.Company;
import com.myforment.companies.models.Employee;
import com.myforment.companies.models.Permission;
import com.myforment.companies.models.Role;
import com.myforment.companies.models.enums.ERole;
import com.myforment.companies.services.CompanyService;
import com.myforment.users.models.Contract;
import com.myforment.users.models.Utente;
import com.myforment.users.multitenant.MongoTemplateCustom;
import com.myforment.users.payload.response.MessageResponse;
import com.myforment.users.security.configuration.Properties;
import com.myforment.users.services.UserDetailsServiceImpl;


/**
 * @author Roberto97
 *Controller of Companies.
 * All the methods in this controller are under the path "/api/companies" and in particular each one has it's relative path.
 * All the methods are also with a different http method between GET, POS and DELETE.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/companies")
public class CompaniesController {
	
	@Autowired
	CompanyService companyService;
	
	@Autowired(required=false)
	HttpServletRequest request;
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	@Qualifier("utentiTemplate")
	private MongoTemplateCustom utentiTemplate;
	
	
	
	
	
	
	/**
	 * Method used to insert a client in the DB.
	 * It get, by post request, the client object and thanks to the Salva method of the clientiService, it add the client to the DB
	 * @param NewCompany  Oggetto company creato nella form react
	 * @return Entity of Company type and CREATED status
	 */
	@PostMapping(value= "/insert", produces = "application/json")
	public ResponseEntity<?> insertCom(@Valid @RequestBody Company NewCompany){		
		
		//Check the company doesn't exists yet
		if (companyService.existByName(NewCompany.getName())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Name is already taken!"));
		}	
		if (companyService.existByLegalName(NewCompany.getLegalName())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Legal Name is already taken!"));
		}
		
		
		
		
		//Assign as owner of the company the logged user
		Utente owner = new Utente();
		
		if(utentiTemplate != null && utentiTemplate.getMultitenantMongoDbFactory().getDefaultId() != null) {

			owner = userDetailsService.loadUtenteModelById(utentiTemplate.getMultitenantMongoDbFactory().getDefaultId());
			
		}else {
			return ResponseEntity.badRequest().body(new MessageResponse("Impossibile associare un utente all'azienda"));
		}
		
		
		Timestamp startDate = new Timestamp(System.currentTimeMillis());
		
		
		NewCompany.setOwner(owner);//Assign owner		
		NewCompany.setCreationDate(startDate); //Set creation date now.
		
		
		
		//Save company into the general database
		Company company = companyService.GeneralSave(NewCompany);
		
		this.generateOwnerRole(company);
		
		//Add Employee to the company. Here the personal tenant database of the company has been created
		Employee e = companyService.addEmployee(owner, company);
		
		//A new contract is created and assigned to the logged user (owner)
		userDetailsService.addContract(owner, new Contract(company.get_id().toString(), e.getRoles(), startDate, false, Properties.OWNER_JOB));
		
		
		
		return new ResponseEntity<Company>(company, HttpStatus.CREATED);
		
	}
	
	public Role generateOwnerRole(Company company){
		Role role = new Role();
		
		ArrayList<Permission> allPermissions = companyService.getAllPermissions();
		
		role.setName(ERole.ROLE_OWNER);
		role.setPermissions(allPermissions);
		
		
		companyService.addRole(role, company);
		
		return role;
	}

}
