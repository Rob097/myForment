package com.myforment.users.models;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Roberto97
 * Entity of Contract. It is inside the user database and contains the relationship between the user and a company
 */
@Document(collection = "contracts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contract {
	
	@Id
	private String id;
	
	@NotBlank
	private String companyId;
	
	@DBRef
	private Set<?> roles = new HashSet<>();	
	
	//Data di inizio del contratto. è obbligatoria
	@NotBlank
	private Timestamp startDate;
	
	//Il contratto si rinnova?
	@NotBlank
	private boolean isRenewable;
	
	//Se il contratto si rinnova, ogni quanti giorni?
	private String renewalCadence;
	
	//Se il contratto non si rinnova, quando termina?
	private Timestamp endDate;
	
	@NotBlank
	private String jobType;
	
	
	/* CONSTRUCTORS */

	public Contract(@NotBlank String companyId, Set<?> roles, @NotBlank Timestamp startDate,
			@NotBlank boolean isRenewable, String renewalCadence, @NotBlank String jobType) {
		super();
		this.companyId = companyId;
		this.roles = roles;
		this.startDate = startDate;
		this.isRenewable = isRenewable;
		this.renewalCadence = renewalCadence;
		this.jobType = jobType;
	}

	public Contract(@NotBlank String companyId, Set<?> roles, @NotBlank Timestamp startDate,
			@NotBlank boolean isRenewable, Timestamp endDate, @NotBlank String jobType) {
		super();
		this.companyId = companyId;
		this.roles = roles;
		this.startDate = startDate;
		this.isRenewable = isRenewable;
		this.endDate = endDate;
		this.jobType = jobType;
	}
	
	public Contract(@NotBlank String companyId, Set<?> roles, @NotBlank Timestamp startDate,
			@NotBlank boolean isRenewable, @NotBlank String jobType) {
		super();
		this.companyId = companyId;
		this.roles = roles;
		this.startDate = startDate;
		this.isRenewable = isRenewable;
		this.jobType = jobType;
	}
	
	
	
}
