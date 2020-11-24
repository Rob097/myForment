package com.myforment.users.multitenant;

//import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

	private HttpServletRequest request;

	private final String USER_ID_ATTRIBUTE = Properties.USER_ID_ATTRIBUTE;
	private final String USER_SIGNUP_ATTRIBUTE = Properties.USER_SIGNUP_ATTRIBUTE;

	private String databaseName;
	
	
	

	public MultitenantMongoDbFactory(MongoClient mongoClient, String databaseName) {
		super((com.mongodb.client.MongoClient) mongoClient, databaseName);
	}

	

	@Autowired(required = false)
	public String resolveEntityId() {
		
		try {

			this.request = this.getRequest();

			if(this.request != null) {
				if (this.request.getAttribute(USER_SIGNUP_ATTRIBUTE) != null && this.request.getAttribute(USER_SIGNUP_ATTRIBUTE).equals(true)) {
					if (this.request.getAttribute(USER_ID_ATTRIBUTE) != null) {
						
						this.request.removeAttribute(USER_SIGNUP_ATTRIBUTE);
						this.databaseName = Properties.databaseUsers;
						return this.request.getAttribute(USER_ID_ATTRIBUTE).toString();
						
					}
				}
			}

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Unexpected value: " + databaseName);
		}
		
		return null;

	}

	@Override
	public MongoDatabase getMongoDatabase() throws DataAccessException {

		MongoDatabase database;

		String entityId = resolveEntityId();

		if (entityId == null) {
			database = super.getMongoDatabase();
		} else {
			database = doGetMongoDatabase(Properties.databasePrefix + "_" + databaseName + "_" + entityId);
		}

		if (database != null) {
			return database;
		} else {
			return super.getMongoDatabase();
		}

	}	
	
	public HttpServletRequest getRequest() {
		RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
		if (RequestContextHolder.getRequestAttributes() != null) {
			return ((ServletRequestAttributes) attribs).getRequest();
		}
		return null;
	}

}
