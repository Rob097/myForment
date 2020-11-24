package com.myforment.users.security.configuration;

import org.springframework.beans.factory.annotation.Value;

public class Properties {

	@Value("${jwtExpirationMs}")
	public static int jwtExpirationMs;
	
	@Value("${jwtExpirationMsRememberMe}")
	public static long jwtExpirationMsRememberMe;
	
	@Value("${jwtSecret}")
	public static String jwtSecret;	
	
	@Value("${jwtHeader}")
	public static String tokenHeader;
	
	@Value("${jwtConstant}")
	public static String tokenConstant;
	
	
	@Value("${connection.host}")
	public static String domainNameCookies;
	
	@Value("${connection.database}")
	public static String database;
	
	@Value("${connection.general.database}")
	public static String databaseGeneral;
	public static String databasePrefix = databaseGeneral;
	
	@Value("${connection.users.database}")
	public static String databaseUsers;
	
	@Value("${connection.uri}")
	public static String connectionUri;
	
	
	
	
	
	
	
	
	
	
	
	
	/* CONSTANTS */
	public static String PATH_COOKIES = "/";
	
	public static String Id_AUTHORITIES = "authority";
	
	public static String CLAIM_KEY_AUTHORITIES = "roles";
	public static String USER_ID_ATTRIBUTE = "userId";
	public static String USER_SIGNUP_ATTRIBUTE = "user_signup";
	
	public static String TOKEN_COOKIE_NAME = "token";
	public static String REMEMBER_COOKIE_NAME = "rememberMe";
	
	
	
}
