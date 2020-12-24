package com.myforment.companies.repository;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;
import com.myforment.companies.models.Company;
import com.myforment.companies.models.Team;
import com.myforment.users.multitenant.MongoTemplateCustom;

@Repository(value="customTeams")
public class TeamRepositoryCustomImpl implements TeamRepositoryCustom{

	@Autowired
	@Qualifier("companyTemplate")
	MongoTemplateCustom companyTemplate;

	@Autowired
	@Qualifier("mongoTemplate")
	MongoTemplate mongoTemplate;
	
	
	@Override
	public ArrayList<Team> getAllTeams(String companyId) throws Exception {
		return (ArrayList<Team>) companyTemplate.setDatabaseName(companyId).findAll(Team.class);
	}
	
	@Override
	public Team createNewTeam(Team t, String companyId) throws Exception {
		return companyTemplate.setDatabaseName(companyId).insert(t);
	}

	@Override
	public DeleteResult removeTeam(String teamId, String companyId) throws Exception {
		Query query = new Query().addCriteria(Criteria.where("_id").is(teamId));
		return companyTemplate.setDatabaseName(companyId).remove(query, Team.class);
	}

}
