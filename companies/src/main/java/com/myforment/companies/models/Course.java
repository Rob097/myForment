package com.myforment.companies.models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Roberto97
 * Entity of Course. It is inside the company database and contains all the information of a specific course
 */
@Document(collection = "courses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
	
	@Id
	private String id;
	
	@DBRef
	private Team team;
	
	private boolean isParent;
	
	@NotBlank
	private String text;
	
	@DBRef
	private List<Course> steps = new ArrayList<>();
	
}
