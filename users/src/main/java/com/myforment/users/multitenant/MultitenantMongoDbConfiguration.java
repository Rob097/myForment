package com.myforment.users.multitenant;

import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClientFactory;
import com.mongodb.client.MongoClients;
import com.myforment.users.security.configuration.Properties;

/**
 * Configuration class to expose @{link MultitenantMongoDbFactory} as a Spring bean.
 * 
 * @author Rauffer Lobo
 */

@Configuration
@ConditionalOnProperty(name="spring.boot.multitenant.mongodb.enabled", havingValue="true", matchIfMissing=true)
public class MultitenantMongoDbConfiguration {
	
	@Autowired
	public HttpServletRequest request;
    
    private com.mongodb.client.MongoClient mongoClient;
    
    @Autowired(required = false)
    private MongoClientFactory mongoClientFactory;
    
    
    

	@Bean
    public MongoClientFactory mongoClientFactory() {
		
		return mongoClientFactory;    	
		
    }
    
    @Bean
    public MongoClient createMongoClient() throws UnknownHostException {
    	
    	if(mongoClient == null) {
    		mongoClient = MongoClients.create(Properties.connectionUri);
    	}
    	return mongoClient;
    	
    }

    @Bean
    public MongoDatabaseFactory mongoDbFactory(String dbName) throws UnknownHostException {
    	
    	return new MultitenantMongoDbFactory(createMongoClient(), dbName);
    	
    }
    
    
   
    
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory(Properties.databaseGeneral));
    }
    
    @Bean
    public MongoTemplate utentiTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory(Properties.databaseUsers));
    }

}