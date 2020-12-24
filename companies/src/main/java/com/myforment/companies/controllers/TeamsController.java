package com.myforment.companies.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.client.result.DeleteResult;
import com.myforment.companies.models.Team;
import com.myforment.companies.services.CompanyService;
import com.myforment.companies.services.TeamService;
import com.myforment.users.models.Utente;
import com.myforment.users.payload.response.MessageResponse;
import com.myforment.users.services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teams")
public class TeamsController {

	@Autowired
	HttpServletRequest request;

	@Autowired
	CompanyService companyService;

	@Autowired
	TeamService teamService;

	@Autowired
	@Qualifier("currentUsersService")
	UserService userDetailsService;

	@Autowired
	@Qualifier("externalUsersService")
	UserService externalUserService;

	@Autowired
	CompaniesController companiesController;

	@GetMapping(value = "/getAllTeams/{companyId}", produces = "application/json")
	public ResponseEntity<?> getAllTeams(@PathVariable("companyId") String companyId) {

		try {

			return new ResponseEntity<ArrayList<Team>>(teamService.getAllTeams(companyId), HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new MessageResponse("Impossibile creare un nuovo team!"));
		}

	}

	@PostMapping(value = "/addTeam", produces = "application/json")
	public ResponseEntity<?> createNewTeam(@Valid @RequestBody Team team) {

		try {

			String companyId = request.getHeader("companyId").toString();

			Utente leader = companiesController.getCurrentUser();

			List<String> leaders = new ArrayList<>();

			leaders.add(leader.getId());

			team.setTeamLeadersId(leaders);

			return new ResponseEntity<Team>(teamService.createNewTeam(team, companyId), HttpStatus.CREATED);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new MessageResponse("Impossibile creare un nuovo team!"));
		}

	}

	@PostMapping(value = "/removeTeam", produces = "application/json")
	public ResponseEntity<?> removeTeam(@Valid @RequestBody String teamId) {

		try {
			
			String companyId = request.getHeader("companyId").toString();

			return new ResponseEntity<DeleteResult>(teamService.removeTeam(teamId.replace("=", ""), companyId), HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new MessageResponse("Impossibile rimuovere questo team!"));
		}

	}

}
