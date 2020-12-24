package com.myforment.users.models;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.myforment.users.models.enums.EGender;
import com.myforment.users.models.enums.ENationality;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Roberto97
 * Entity of Utente. It is inside the user database and contains all the information of the user
 */
@Document(collection = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Utente {
	
	@Id
	private String id;

	@NotBlank
	@Size(max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;
	
	@NotBlank
	private String name;
	@NotBlank
	private String surname;
	
	private EGender gender;
	private String phone;
	private Address address;
	private ENationality nationality;
	private String avatar;
	
	private Timestamp birthDate;

	private Set<String> rolesId = new HashSet<>();
	
	
	
	
	/* CONSTRUCTORS */
	
	public Utente(@NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 50) @Email String email,
			@NotBlank @Size(max = 120) String password, @NotBlank String nome, @NotBlank String cognome) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
	}
	
	public Utente(User u) {
		this.id = u.getId();
		this.username = u.getUsername();
		this.email = u.getEmail();
		this.password = u.getPassword();
		this.name = u.getUsername();
		this.surname = "";
		for (Role role : u.getRoles()) {
			this.rolesId.add(role.getId());
		}
	}
	
	

}
