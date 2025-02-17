package com.myforment.companies.models;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.myforment.companies.models.enums.ERole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

	@Id
	private String id;

	@NotBlank
	private ERole roleName;

	@NotBlank
	private List<String> permissionsId;

	public Role(@NotBlank ERole roleName, @NotBlank List<String> permissionsId) {
		super();
		this.roleName = roleName;
		this.permissionsId = permissionsId;
	}

}
