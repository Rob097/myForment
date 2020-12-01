package com.myforment.companies.controllers;

import java.util.Date;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
 * @author Roberto97 Controller of Companies. All the methods in this controller
 *         are under the path "/api/companies" and in particular each one has
 *         it's relative path. All the methods are also with a different http
 *         method between GET, POS and DELETE.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/companies")
public class CompaniesController {

	@Autowired
	CompanyService companyService;

	@Autowired(required = false)
	HttpServletRequest request;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	@Qualifier("utentiTemplate")
	private MongoTemplateCustom utentiTemplate;

	@RequestMapping(value = "/cerca/all", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> listAllCom() {

		Utente utente = getCurrentUser();

		if (utente == null)
			return ResponseEntity.badRequest().body(new MessageResponse("Impossibile trovare l'utente corrente!"));

		ArrayList<Contract> allContracts = userDetailsService.getContracts(utente);
		ArrayList<Contract> ownerContracts = new ArrayList<>();

		if (allContracts == null || allContracts.isEmpty()) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Non è stato possibile recuperare i tuoi contratti!"));
		}

		for (Contract contract : allContracts) {
			for (String roleId : contract.getRolesId()) {
				Role role = companyService.getRoleById(roleId, contract.getCompanyId());
				if (role != null && role.getName().equals(ERole.ROLE_OWNER))
					ownerContracts.add(contract);
			}
		}

		ArrayList<Company> companies = new ArrayList<>();

		for (Contract contract : ownerContracts) {
			companies.add(companyService.GetById(contract.getCompanyId()));
		}

		if (companies == null || companies.isEmpty()) {
			return ResponseEntity.badRequest().body(new MessageResponse("Non è stato trovato alcuna azienda!"));
		}

		return new ResponseEntity<ArrayList<Company>>(companies, HttpStatus.OK);

	}

	@RequestMapping(value = "/cerca/id/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getComById(@PathVariable("id") String Id) {

		Company company = companyService.GetById(Id);

		if (company == null) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Non è stato trovato alcuna azienda con qusto id!"));
		}

		return new ResponseEntity<Company>(company, HttpStatus.OK);

	}

	/**
	 * Method used to insert a client in the DB. It get, by post request, the client
	 * object and thanks to the Salva method of the clientiService, it add the
	 * client to the DB
	 * 
	 * @param NewCompany Oggetto company creato nella form react
	 * @return Entity of Company type and CREATED status
	 */
	@PostMapping(value = "/insert", produces = "application/json")
	public ResponseEntity<?> insertCom(@Valid @RequestBody Company NewCompany) {
		System.out.println("company id: " + NewCompany.getIdCom());
		if (NewCompany.getIdCom() != null) {
			System.out.println("Modifico una company");
			Company company = companyService.GeneralSave(NewCompany);
			return new ResponseEntity<Company>(company, HttpStatus.CREATED);
		} else {

			// Check the company doesn't exists yet
			if (companyService.existByName(NewCompany.getName())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Name is already taken!"));
			}
			if (companyService.existByLegalName(NewCompany.getLegalName())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Legal Name is already taken!"));
			}

			// Assign as owner of the company the logged user
			Utente owner = getCurrentUser();

			if (owner == null) {
				return ResponseEntity.badRequest()
						.body(new MessageResponse("Impossibile associare un utente all'azienda"));
			}

			Date startDate = new Date();

			NewCompany.setOwnerId(owner.getId());// Assign owner
			// NewCompany.setCreationDate(startDate); // Set creation date now.

			// Save company into the general database
			Company company = companyService.GeneralSave(NewCompany);

			this.generateOwnerRole(company);

			// Add Employee to the company. Here the personal tenant database of the company
			// has been created
			Employee e = companyService.addEmployee(owner, company);

			ArrayList<String> employeeRolesId = new ArrayList<>();
			for (Object r : e.getRoles()) {
				Role role = (Role) r;
				employeeRolesId.add(role.getId());
			}

			// A new contract is created and assigned to the logged user (owner)
			userDetailsService.addContract(owner,
					new Contract(company.getIdCom(), employeeRolesId, startDate, false, Properties.OWNER_JOB));

			return new ResponseEntity<Company>(company, HttpStatus.CREATED);
		}

	}

	public Role generateOwnerRole(Company company) {
		Role role = new Role();

		ArrayList<Permission> allPermissions = companyService.getAllPermissions();
		ArrayList<String> allPermissionsId = new ArrayList<>();
		role.setName(ERole.ROLE_OWNER);

		for (Permission permission : allPermissions) {
			allPermissionsId.add(permission.getId());
		}

		role.setPermissionsId(allPermissionsId);

		companyService.addRole(role, company);

		return role;
	}

	public Utente getCurrentUser() {
		Utente utente = new Utente();

		if (utentiTemplate != null && utentiTemplate.getMultitenantMongoDbFactory().getDefaultId() != null) {

			utente = userDetailsService
					.loadUtenteModelById(utentiTemplate.getMultitenantMongoDbFactory().getDefaultId());

			return utente;

		} else {
			return null;
		}
	}

}
