package com.myforment.users.multitenant;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.myforment.users.security.configuration.Properties;

/*
 * Custom Class for Mongo Template. It allows me to set entity id to find dynamically the database.
 */
public class MongoTemplateCustom extends MongoTemplate {
	
	private final MultitenantMongoDbFactory multitenantMongoDbFactory;

	private String databaseName;

	private Properties properties;
	
	
	
	
	
	//============================================================================================================================
	
	public MongoTemplateCustom(MultitenantMongoDbFactory multitenantMongoDbFactory) {
		super(multitenantMongoDbFactory);
		this.multitenantMongoDbFactory = multitenantMongoDbFactory;
		this.databaseName = multitenantMongoDbFactory.getDatabaseName();
		this.properties = multitenantMongoDbFactory.getProperties();		
	}
	
	//============================================================================================================================

	public MultitenantMongoDbFactory getMultitenantMongoDbFactory() {
		return multitenantMongoDbFactory;
	}

	//============================================================================================================================
	
	//Method to set default database for user template. It also sets the default user id.
	public void setDefaultUserDb(String userId) {
		if(this.databaseName.equals(this.properties.getDatabaseUsers()) && userId != null && !userId.isEmpty()) {
			this.getMultitenantMongoDbFactory().setDatabaseName(this.properties.getDatabasePrefix() + "_" + this.databaseName + "_" + userId);
			this.getMultitenantMongoDbFactory().setDefaultId(userId);
		}
	}
	
	//============================================================================================================================
	
	//Method used to set the datbase name. By default user template don't need it because we auomatically use the connected user.
	//But you can use this method with user template if you need to work with another account.
	public MongoTemplateCustom setDatabaseName(String entityId) {

		//If entityId is null or this database is general database or some paramethers are null or empty, set database name as general database
		if (this.properties == null || this.properties.getDatabaseGeneral() == null || this.properties.getDatabaseGeneral().isEmpty()
			|| this.properties.getDatabasePrefix() == null || this.properties.getDatabasePrefix().isEmpty()
			|| this.databaseName == null || this.databaseName.isEmpty()
			|| entityId == null || entityId.isEmpty()
			|| this.databaseName.equals(this.properties.getDatabaseGeneral())) {
			
			this.getMultitenantMongoDbFactory().setDatabaseName(this.databaseName);
			
		}
		
		//Else, if entityId isn't null and this database is user, then set temporarydatabase with entityId param. (by default database name is set with current logged user id)
		else if(entityId != null && this.databaseName.equals(this.properties.getDatabaseUsers())) {
			this.getMultitenantMongoDbFactory().setTemporaryDatabaseName(this.properties.getDatabasePrefix() + "_" + this.databaseName + "_" + entityId);
		}
		
		//Else set normal database name with this database name and entityId
		else {
			this.getMultitenantMongoDbFactory().setDatabaseName(this.properties.getDatabasePrefix() + "_" + this.databaseName + "_" + entityId);
		}
		
		return this;
	}
	
	//============================================================================================================================

}
