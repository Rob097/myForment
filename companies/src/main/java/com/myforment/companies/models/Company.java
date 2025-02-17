package com.myforment.companies.models;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.myforment.users.models.Address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Roberto97
 * Entity of Company. It is inside the company database and contains all the information of the company
 */
@Document(collection = "companies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
	
	@Id
	private String id;
	
	@NotBlank
	private String ownerId;
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String logo;
	
	private Address address;
	
	private String legalName;
	
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	private String sector;
	
	
	
	
}
