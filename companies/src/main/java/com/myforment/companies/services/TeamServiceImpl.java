package com.myforment.companies.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.mongodb.client.result.DeleteResult;
import com.myforment.companies.models.Team;
import com.myforment.companies.repository.TeamRepositoryCustom;

@Service
public class TeamServiceImpl implements TeamService{

	@Autowired
	@Qualifier("customTeams") //Annotation used to define which interface implementation to use
	private TeamRepositoryCustom teamRepository;
	
	
	@Override
	public ArrayList<Team> getAllTeams(String companyId) throws Exception {
		return teamRepository.getAllTeams(companyId);
	}
	
	@Override
	public Team createNewTeam(Team t, String companyId) throws Exception{
		return teamRepository.createNewTeam(t, companyId);
	}

	@Override
	public DeleteResult removeTeam(String teamId, String companyId) throws Exception {
		return teamRepository.removeTeam(teamId, companyId);		
	}
	
}
