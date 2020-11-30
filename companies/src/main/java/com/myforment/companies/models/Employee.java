package com.myforment.companies.models;

import java.util.HashSet;
import java.util.Set;

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
 * Entity of Employy. It is inside the company database and contains all the information of the employee
 */
@Document(collection = "employees")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
	
	@Id
	private String id;
	
	@NotBlank
	@DBRef
	private Utente user;
	
	@DBRef
	private Set<Role> roles = new HashSet<>();

	public Employee(@NotBlank Utente user, Set<Role> roles) {
		super();
		this.user = user;
		this.roles = roles;
	}	

}
