package com.myforment.companies.models.enums;

/**
 * @author Roberto97
 * List of permissions that a COMPANY role can have
 */
public enum EPermission {
	ADD_EMPLOYEE,
	ADD_TRAINEE,
	ADD_COLLABORATOR,
	REMOVE_EMPLOYEE,
	REMOVE_TRAINEE,
	REMOVE_COLLABORATOR,
	ASSIGN_PERMISSIONS,
	UPDATE_PERMISSIONS,
	CREATE_TEAM,
	ASSIGN_TEAM, //Assign employee to a team
	ASSIGN_SUPERVISOR, //Assign supervisor to a team
	
	CREATE_COURSE,
	EDIT_COURSE,
	TEST_COURSE,
	REMOVE_COURSE,
	ATTEND_COURSE,
	UPLOAD_DOCS, //Upload documentation
	REMOVE_DOCS, //Remove documentation
	PUBLISH_ADS, //Publish announcement
	CHECK_COURSE_PROGRESS //Check the progress of employees on a course
	
}
