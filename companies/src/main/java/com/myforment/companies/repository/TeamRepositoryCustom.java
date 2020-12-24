package com.myforment.companies.repository;

import java.util.ArrayList;

import com.mongodb.client.result.DeleteResult;
import com.myforment.companies.models.Team;

public interface TeamRepositoryCustom {
	
	ArrayList<Team> getAllTeams(String companyId) throws Exception;

	Team createNewTeam(Team t, String companyId) throws Exception;
	
	DeleteResult removeTeam(String teamId, String companyId) throws Exception;
	
}
