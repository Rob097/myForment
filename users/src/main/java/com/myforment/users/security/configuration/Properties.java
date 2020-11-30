package com.myforment.users.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Properties {

	private final int jwtExpirationMs;
	
	private final long jwtExpirationMsRememberMe;

	private final String jwtSecret;	

	private final String tokenHeader;
	
	private final String tokenConstant;
	
	private final String domainNameCookies;
	
	private final String database;
	
	private final String databaseGeneral;
	private final String databasePrefix;
	
	private final String databaseUsers;
	
	private final String databaseCompanies;
	
	private final String connectionUri;
    
    
    
    @Autowired
    public Properties(
    		@Value("${jwtExpirationMs}") int jwtExpirationMs,
    		@Value("${jwtExpirationMsRememberMe}") long jwtExpirationMsRememberMe,
    		@Value("${jwtSecret}") String jwtSecret,
    		@Value("${jwtHeader}") String tokenHeader,
    		@Value("${jwtConstant}") String tokenConstant,
    		@Value("${connection.host}") String domainNameCookies,
    		@Value("${connection.database}") String database,
    		@Value("${connection.general.database}") String databaseGeneral,
    		@Value("${connection.users.database}") String databaseUsers,
    		@Value("${connection.company.database}") String databaseCompanies,
    		@Value("${connection.uri}") String connectionUri    		
    		) {
    	this.jwtExpirationMs = jwtExpirationMs;
    	this.jwtExpirationMsRememberMe = jwtExpirationMsRememberMe;
    	this.jwtSecret = jwtSecret;
    	this.tokenHeader = tokenHeader;
    	this.tokenConstant = tokenConstant;
    	this.domainNameCookies = domainNameCookies;
    	this.database = database;
    	this.databasePrefix = databaseGeneral;
    	this.databaseGeneral = databaseGeneral;
    	this.databaseUsers = databaseUsers;
    	this.databaseCompanies = databaseCompanies;
    	this.connectionUri = connectionUri;
    	
    }
	
	
	
	
	/* CONSTANTS */
	public static String PATH_COOKIES = "/";
	
	public static String Id_AUTHORITIES = "authority";
	
	public static String CLAIM_KEY_AUTHORITIES = "roles";
	public static String USER_ID_ATTRIBUTE = "userId";
	public static String ENTITY_ID_ATTRIBUTE = "entity_id";
	
	public static String TOKEN_COOKIE_NAME = "token";
	public static String REMEMBER_COOKIE_NAME = "rememberMe";
	
	public static String OWNER_JOB = "Owner";
	
	//ROLES
	public static String ROLE_BASIC = "BASIC";
	public static String ROLE_ADMIN = "ADMIN";
	public static String ROLE_EDITOR = "EDITOR";
}
