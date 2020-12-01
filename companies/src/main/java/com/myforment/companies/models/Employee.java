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
	private String userId;
	
	//DBRef works here because company roles are in the same database of employee
	@DBRef 
	private List<?> roles = new ArrayList<>();

	public Employee(@NotBlank String userId, List<?> roles) {
		super();
		this.userId = userId;
		this.roles = roles;
	}	

}
