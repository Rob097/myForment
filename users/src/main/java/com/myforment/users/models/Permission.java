package com.myforment.users.models;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.myforment.users.models.enums.EPermission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "permissions")
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
