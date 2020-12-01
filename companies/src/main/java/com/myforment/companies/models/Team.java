package com.myforment.companies.models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.myforment.users.models.Utente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Roberto97
 * Entity of Team. It is inside the company database and contains all the information of a specific team (dipartimento)
 */
@Document(collection = "teams")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team {
	
	@Id
	private String id;
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String description;
	
	@NotBlank
	private List<String> teamLeadersId = new ArrayList<>();
	
	private List<String> teamMembersId = new ArrayList<>();
	
	
	
	/* CONSTRUCOTRS */
	public Team(String id, @NotBlank String name, @NotBlank String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}


	public Team(String id, @NotBlank String name, @NotBlank String description, List<String> teamLeadersId) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.teamLeadersId = teamLeadersId;
	}
	
	
}
