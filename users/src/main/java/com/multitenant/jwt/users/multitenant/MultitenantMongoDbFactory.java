package com.multitenant.jwt.users.multitenant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import com.mongodb.DB;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import com.multitenant.jwt.users.security.jwt.JwtUtils;

/**
 * Factory to create {@link DB} instances based on the user's request.
 * 
 * @author Rauffer Lobo
 */
public class MultitenantMongoDbFactory extends SimpleMongoClientDatabaseFactory implements MongoDatabaseFactory {

	@Autowired
	private HttpServletRequest request;
	
	final String TENANT_ID_ATTRIBUTE = JwtUtils.TENANT_ID_ATTRIBUTE;

	@Value("${spring.data.mongodb.database}")
	private String databasePrefix;

	public MultitenantMongoDbFactory(MongoClient mongoClient, String databaseName) {
		super((com.mongodb.client.MongoClient) mongoClient, databaseName);
	}

	@Autowired(required = false)
	public String tenantResolver() {
		
		String tenantId = null;
		try {
			if (this.request.getAttribute(TENANT_ID_ATTRIBUTE) != null) {

				tenantId = this.request.getAttribute(TENANT_ID_ATTRIBUTE).toString(); // implement your own strategy
				
			}
		} catch (Exception e) {}
		return tenantId;

	}

	@Override
	public MongoDatabase getMongoDatabase() throws DataAccessException {

		MongoDatabase database;
		
		String tenant = tenantResolver();

		if (tenant == null) {
			database = super.getMongoDatabase();
		}else {
			database = doGetMongoDatabase(databasePrefix + tenant);	
		}
		
		if(database != null) {
			return database;
		}else {
			return super.getMongoDatabase();
		}

	}

}
