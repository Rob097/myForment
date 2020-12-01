package com.myforment.users.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
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
	
	private List<String> rolesId = new ArrayList<>();	
	
	//Data di inizio del contratto. è obbligatoria
	@NotBlank
	private Date startDate;
	
	//Il contratto si rinnova?
	@NotBlank
	private boolean isRenewable;
	
	//Se il contratto si rinnova, ogni quanti giorni?
	private String renewalCadence;
	
	//Se il contratto non si rinnova, quando termina?
	private Date endDate;
	
	@NotBlank
	private String jobType;
	
	
	/* CONSTRUCTORS */

	public Contract(@NotBlank String companyId, List<String> rolesId, @NotBlank Date startDate, @NotBlank boolean isRenewable, String renewalCadence, @NotBlank String jobType) {
		super();
		this.companyId = companyId;
		this.rolesId = rolesId;
		this.startDate = startDate;
		this.isRenewable = isRenewable;
		this.renewalCadence = renewalCadence;
		this.jobType = jobType;
	}

	public Contract(@NotBlank String companyId, List<String> rolesId, @NotBlank Date startDate, @NotBlank boolean isRenewable, Date endDate, @NotBlank String jobType) {
		super();
		this.companyId = companyId;
		this.rolesId = rolesId;
		this.startDate = startDate;
		this.isRenewable = isRenewable;
		this.endDate = endDate;
		this.jobType = jobType;
	}
	
	public Contract(@NotBlank String companyId, List<String> rolesId, @NotBlank Date startDate, @NotBlank boolean isRenewable, @NotBlank String jobType) {
		super();
		this.companyId = companyId;
		this.rolesId = rolesId;
		this.startDate = startDate;
		this.isRenewable = isRenewable;
		this.jobType = jobType;
	}
	
	
	
}
