package com.myforment.users.multitenant;

import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.myforment.users.encryption.MongoDBAfterLoadEventListener;
import com.myforment.users.encryption.MongoDBBeforeSaveEventListener;
import com.myforment.users.security.configuration.Properties;

/**
 * Configuration class to expose @{link MultitenantMongoDbFactory} as a Spring
 * bean.
 * 
 * @author Rauffer Lobo
 */

@Configuration
@ConditionalOnProperty(name = "spring.boot.multitenant.mongodb.enabled", havingValue = "true", matchIfMissing = true)
public class MultitenantMongoDbConfiguration{

	@Autowired
	public HttpServletRequest request;

	@Autowired
	private Properties properties;

	private static com.mongodb.client.MongoClient mongoClient;


	@Bean
	public MongoClient createMongoClient() throws UnknownHostException {
		
		if (mongoClient == null) {
			mongoClient = MongoClients.create(properties.getConnectionUri());
		}

		return mongoClient;

	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(
				new MultitenantMongoDbFactory(createMongoClient(), properties.getDatabaseGeneral(), this.properties));
	}

	@Bean
	public MongoTemplateCustom utentiTemplate() throws Exception {		
		return new MongoTemplateCustom(
				new MultitenantMongoDbFactory(createMongoClient(), properties.getDatabaseUsers(), this.properties));
	}

	@Bean
	public MongoTemplateCustom companyTemplate() throws Exception {
		return new MongoTemplateCustom(
				new MultitenantMongoDbFactory(createMongoClient(), properties.getDatabaseCompanies(), this.properties));
	}

	

	
	/* ENCRYPTION BEANS */
	/*@Bean
	public EncryptionUtil encryptionUtil() throws Exception {
		return new EncryptionUtil(properties.getEncryptPassword());
	}*/
	@Bean
	public MongoDBBeforeSaveEventListener mongoDBBeforeSaveEventListener() {
	    return new MongoDBBeforeSaveEventListener();
	}

	@Bean
	public MongoDBAfterLoadEventListener mongoDBAfterLoadEventListener() {
	    return new MongoDBAfterLoadEventListener();
	}
	
	
}