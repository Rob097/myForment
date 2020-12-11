package com.myforment.users.models;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import com.myforment.users.models.enums.ERole;

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
  private ERole name;
  
  //DBRef works here because permissions are in the same database of role
  @DBRef 
  @NotBlank
  private List<Permission> permissions;
}
