package com.myforment.companies.models;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.myforment.companies.models.enums.EPermission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "companiesPermissions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission {

	@Id
	private String id;
	
	@NotBlank
	private EPermission name;
	
	private String description;
	
}
