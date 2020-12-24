package com.myforment.companies.services;

import java.util.ArrayList;

import com.mongodb.client.result.DeleteResult;
import com.myforment.companies.models.Team;

public interface TeamService {
	
	public ArrayList<Team> getAllTeams(String companyId) throws Exception;

	public Team createNewTeam(Team t, String companyId) throws Exception;
	
	public DeleteResult removeTeam(String teamId, String companyId) throws Exception;
	
}
