package com.myforment.users.models;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Roberto97
 * Entity of Utente. It is inside the user database and contains all the information of the user
 */
@Document(collection = "utente")
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
	private String nome;
	@NotBlank
	private String cognome;
	
	private Gender gender;
	private String telefono;
	private String indirizzo;
	private Nationality nazionalità;
	private String avatar;

	@DBRef
	private Set<Role> roles = new HashSet<>();	

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
		this.nome = u.getUsername();
		this.cognome = "";
		this.roles = u.getRoles();
	}
	
	

}
