package com.myforment.companies.models;

import java.sql.Timestamp;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.myforment.users.models.Address;
import com.myforment.users.models.Utente;

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
	private ObjectId _id;
	
	@NotBlank
	@DBRef
	private Utente owner;
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String logo;
	
	@DBRef
	private Address address;
	
	private String legalName;
	
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	private String sector;
	
	@NotBlank
	private Timestamp creationDate;	
	
	
}
