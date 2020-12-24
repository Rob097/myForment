package com.myforment.companies.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.myforment.users.models.User;
import com.myforment.users.models.Utente;
import com.myforment.users.payload.response.MessageResponse;
import com.myforment.users.security.configuration.Properties;
import com.myforment.users.services.UserService;

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

	@Autowired
	@Qualifier("currentUsersService")
	UserService userDetailsService;
	
	@Autowired
	@Qualifier("externalUsersService")
	UserService externalUserService;
	
	
	

	
	//================================================   Get all Companies  ===============================================================
	
	@RequestMapping(value = "/cerca/all", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getAllCompanies() throws Exception {

		ArrayList<Contract> allContracts = userDetailsService.getAllContracts(); //Of current user
		ArrayList<Contract> ownerContracts = new ArrayList<>();
		ArrayList<Company> companies = new ArrayList<>();

		if (allContracts == null || allContracts.isEmpty()) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Non è stato trovato alcun contratto valido!"));
		}

		for (Contract contract : allContracts) {
			for (String roleId : contract.getRolesId()) {
				Role role = companyService.getRoleById(roleId, contract.getCompanyId());
				if (role != null && role.getRoleName().equals(ERole.ROLE_OWNER))
					ownerContracts.add(contract);
			}
		}
		

		for (Contract contract : ownerContracts) {
			companies.add(companyService.getCompanyById(contract.getCompanyId()));
		}

		if (companies == null || companies.isEmpty()) {
			return ResponseEntity.badRequest().body(new MessageResponse("Non è stato trovato alcuna azienda!"));
		}

		return new ResponseEntity<ArrayList<Company>>(companies, HttpStatus.OK);

	}
	
	//=====================================================================================================================================================

	@RequestMapping(value = "/cerca/id/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getCompanyById(@PathVariable("id") String Id) throws Exception {

		Company company = companyService.getCompanyById(Id);

		if (company == null) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Non è stata trovata alcuna azienda con qusto id!"));
		}

		return new ResponseEntity<Company>(company, HttpStatus.OK);

	}
	
	//=====================================================================================================================================================

	/**
	 * Method used to insert a client in the DB. It get, by post request, the client
	 * object and thanks to the Salva method of the clientiService, it add the
	 * client to the DB
	 * 
	 * @param NewCompany Oggetto company creato nella form react
	 * @return Entity of Company type and CREATED status
	 * @throws Exception 
	 */
	@PostMapping(value = "/insert", produces = "application/json")
	public ResponseEntity<?> createNewCompany(@Valid @RequestBody Company NewCompany) throws Exception {
		
		if (NewCompany.getId() != null && !NewCompany.getId().isEmpty()) {

			return new ResponseEntity<Company>(companyService.saveCompany(NewCompany), HttpStatus.CREATED);
			
		} else {

			NewCompany.setId(null); //If you don't set this, it doesn't work

			// Check the company doesn't exists yet
			if (companyService.existCompanyByName(NewCompany.getName())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Name is already taken!"));
			}
			if (companyService.existCompanyByLegalName(NewCompany.getLegalName())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Legal Name is already taken!"));
			}

			// Assign as owner of the company the logged user
			Utente owner = getCurrentUser();

			if (owner == null) {
				return ResponseEntity.badRequest()
						.body(new MessageResponse("Impossibile associare un utente all'azienda"));
			}

			NewCompany.setOwnerId(owner.getId());// Assign owner

			// Save company into the general database
			Company company = companyService.saveCompany(NewCompany);

			//Generate Owner Role
			this.generateCustomRole(company, ERole.ROLE_OWNER, companyService.getAllPermissions());

			// Add Employee to the company. Here the personal tenant database of the company has been created
			Employee e = companyService.addEmployee(owner, company.getId(), ERole.ROLE_OWNER);

			ArrayList<String> employeeRolesId = new ArrayList<>();
			for (Object r : e.getRoles()) {
				Role role = (Role) r;
				employeeRolesId.add(role.getId());
			}

			// A new contract is created and assigned to the logged user (owner)
			userDetailsService.addContract(new Contract(company.getId(), employeeRolesId, new Date(), false, Properties.OWNER_JOB));

			return new ResponseEntity<Company>(company, HttpStatus.CREATED);
		}

	}
	
	//=====================================================================================================================================================

	@DeleteMapping(value = "/elimina/id/{id}", produces = "application/json")
	public ResponseEntity<Void> removeCompanyById(@PathVariable("id") String Id) throws Exception {

		ArrayList<Employee> employees = new ArrayList<>();
		employees = companyService.getAllEmployees(Id);
		for (Employee employee : employees) {
			ArrayList<Contract> contracts = externalUserService.getAllContracts(employee.getUserId());
			for (Contract contract : contracts) {
				if (contract.getCompanyId().equals(Id))
					externalUserService.removeContract(contract, employee.getUserId());
			}
		}
		
		companyService.removecompany(Id);

		return new ResponseEntity<Void>(HttpStatus.OK);

	}
	
	//=====================================================================================================================================================

	@GetMapping(value = "/searchUserToInvite/{companyId}", produces = "application/json")
	public ResponseEntity<?> searchUsersToInvite(@PathVariable("companyId") String companyId) throws Exception {

		return new ResponseEntity<ArrayList<User>>(companyService.loadAllUsersExceptEmployees(companyId), HttpStatus.OK);
		
	}
	
	//=====================================================================================================================================================
	
	@GetMapping(value = "/searchCompanysEmployees/{companyId}", produces = "application/json")
	public ResponseEntity<?> searchCompanysEmployees(@PathVariable("companyId") String companyId) throws Exception {

		ArrayList<Employee> employees = companyService.getAllEmployees(companyId);
		ArrayList<Utente> utenti = new ArrayList<>();
		for (Employee employee : employees) {
			utenti.add(externalUserService.getUtenteModelById(employee.getUserId()));
		}
		return new ResponseEntity<ArrayList<Utente>>(utenti, HttpStatus.OK);
	}	
	
	//=====================================================================================================================================================

	@PostMapping(value = "/invite", produces = "application/json")
	public ResponseEntity<?> inviteNewUser(@RequestBody /*HashMap<String, ArrayList<HashMap<String, String>>>*/ HashMap<?, ?> parameters) {

		try {
			String companyId = parameters.get("companyId").toString();
			@SuppressWarnings("unchecked")
			ArrayList<HashMap<String, String>> c = (ArrayList<HashMap<String, String>>) parameters.get("utenti");

			for (HashMap<String, String> user :  c) {

				String id = user.get("value").toString();
				Utente u = externalUserService.getUtenteModelById(id);
				Company company = companyService.getCompanyById(companyId);
				
				this.generateCustomRole(company, ERole.ROLE_BASIC, companyService.getAllPermissions());
				
				Employee e = companyService.addEmployee(u, company.getId(), ERole.ROLE_BASIC);
				ArrayList<String> employeeRolesId = new ArrayList<>();
				for (Object r : e.getRoles()) {
					Role role = (Role) r;
					employeeRolesId.add(role.getId());
				}

				// A new contract is created and assigned to the logged user (owner)
				externalUserService.addContract(new Contract(company.getId(), employeeRolesId, new Date(), false, Properties.OWNER_JOB), u.getId());

			}

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Impossibile invitare gli utenti nell'azienda"));
		}

	}
	
	//=====================================================================================================================================================
	
	@PostMapping(value = "/removeUser", produces = "application/json")
	public ResponseEntity<?> removeEmployee(@RequestBody HashMap<String, String> parameters) {
		
		try {
			
			String userId = parameters.get("userId").toString();
			String companyId = parameters.get("companyId").toString();
			
			companyService.removeEmployeeByUserId(userId, companyId);
			
			ArrayList<Contract> contracts = externalUserService.getAllContracts(userId);
			for (Contract contract : contracts) {
				if(contract.getCompanyId().equals(companyId))
					externalUserService.removeContract(contract, userId);
			}
			
			return new ResponseEntity<>(HttpStatus.OK);
			
			
		}catch(Exception e) {
			//e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Impossibile rimuovere gli utenti dall'azienda"));
		}
	}
	
	//=====================================================================================================================================================

	public Role generateCustomRole(Company company, ERole roleName, ArrayList<Permission> permissions) throws Exception {
		
		ArrayList<String> permissionsId = new ArrayList<>();
		
		for (Permission permission : permissions) {
			permissionsId.add(permission.getId());
		}
		
		Role role = new Role(ERole.ROLE_OWNER, permissionsId);
		
		companyService.addNewRoleToCompany(role, company.getId());
		
		return role;
	}
	
	//=====================================================================================================================================================

	public Utente getCurrentUser() {
		Utente utente = new Utente();

		try{

			utente = userDetailsService
					.getCurrentUtenteModel();

			return utente;

		} catch(Exception e) {
			return null;
		}
	}
	
	//=====================================================================================================================================================

}
