package com.multitenant.jwt.users.multitenant;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClientFactory;
import com.mongodb.client.MongoClients;

/**
 * Configuration class to expose @{link MultitenantMongoDbFactory} as a Spring bean.
 * 
 * @author Rauffer Lobo
 */

@Configuration
@ConditionalOnProperty(name="spring.boot.multitenant.mongodb.enabled", havingValue="true", matchIfMissing=true)
public class MultitenantMongoDbConfiguration {
    
	@Value("${connection.uri}")
    private String connectionUri;

    @Autowired
    private MongoProperties properties;
    
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
    		mongoClient = MongoClients.create(connectionUri);
    	}
    	return mongoClient;
    	
    }

    @Bean
    public MongoDatabaseFactory mongoDbFactory() throws UnknownHostException {
    	
    	return new MultitenantMongoDbFactory(createMongoClient(), properties.getDatabase());
    	
    }
    
    
    
    /*
     * I MongoTemplate servono per sostituire in parte i repositories e collegarsi a differenti database per alcune operazioni.
     * mongoTemplate() imposta il database principale da usare mentre generalTemplate() serve per collegarsi al database principale anche dopo
     * che un utente ha fatto il login ed è quindi stato impostato come database principale quello personale dell'utente. GG
     */
    @Bean
	public MongoTemplate mongoTemplate() throws UnknownHostException {
    	
    	this.createMongoClient();

		return new MongoTemplate((MongoDatabaseFactory) mongoDbFactory());
		
	}
    
    @Bean
    public MongoTemplate generalTemplate() throws Exception {
        return new MongoTemplate(new MultitenantMongoDbFactory(createMongoClient(), properties.getDatabase()));
    }

}