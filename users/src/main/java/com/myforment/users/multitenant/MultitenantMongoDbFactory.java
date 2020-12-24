package com.myforment.users.multitenant;

import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import com.mongodb.DB;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.myforment.users.security.configuration.Properties;

/**
 * Factory to create {@link DB} instances based on the user's request.
 * 
 * @author Rauffer Lobo
 */
public class MultitenantMongoDbFactory extends SimpleMongoClientDatabaseFactory implements MongoDatabaseFactory {

	private Properties properties;
	private String databaseName;
	private String temporaryDatabaseName;
	private String defaultId;
	private String temporaryId;

	
	
	//============================================================================================================================
	
	public MultitenantMongoDbFactory(MongoClient mongoClient, String databaseName, Properties properties) {
		super((MongoClient) mongoClient, databaseName);
		this.databaseName = databaseName;
		this.properties = properties;
	}
	
	//============================================================================================================================
	
	@Override
	public MongoDatabase getMongoDatabase() throws DataAccessException {

		MongoDatabase database = null;

		//temporaryDatabaseName is used only if userr template needs to access to an user different to the current logged
		if (this.temporaryDatabaseName != null && !this.temporaryDatabaseName.isEmpty()) {
			database = doGetMongoDatabase(this.temporaryDatabaseName);
			this.temporaryDatabaseName = null;
			
		//Else it uses databaseName
		} else if (this.databaseName != null && !this.databaseName.isEmpty()) {
			database = doGetMongoDatabase(this.databaseName);
		}

		//If it occurs in some errors it tryes to call default database
		if (database != null) {
			return database;
		} else {
			return super.getMongoDatabase();
		}

	}
	
	//============================================================================================================================
	// GETTER
	
	public String getDatabaseName() {
		return databaseName;
	}
	
	public String getTemporaryDatabaseName() {
		return temporaryDatabaseName;
	}

	public String getDefaultId() {
		return defaultId;
	}
	
	public String getTemporaryId() {
		return temporaryId;
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	//============================================================================================================================
	//SETTER
	
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public void setTemporaryDatabaseName(String temporaryDatabaseName) {
		this.temporaryDatabaseName = temporaryDatabaseName;
	}

	public void setDefaultId(String defaultId) {
		this.defaultId = defaultId;
	}

	public void setTemporaryId(String temporaryId) {
		this.temporaryId = temporaryId;
	}
	
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	//============================================================================================================================


	/*
	 * public HttpServletRequest getRequest() { RequestAttributes attribs =
	 * RequestContextHolder.getRequestAttributes(); if
	 * (RequestContextHolder.getRequestAttributes() != null) { return
	 * ((ServletRequestAttributes) attribs).getRequest(); } return null; }
	 */
}
