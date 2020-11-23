package com.multitenant.jwt.clients.repository;

import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.multitenant.jwt.clients.models.Clienti;

public interface ClientiRepository extends MongoRepository<Clienti, String>{//ReactiveCrudRepository<Clienti,String> (Serve per qualsiasi altro tipo di Database). String indica il tipo della chiave primaria del documento
	
	//https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
	
	public Clienti findByCodfid(String Codfid);
	
	public ArrayList<Clienti> findByNominativoLike(String Nominativo);
	
	@Query("{ 'cards.bollini': {$gt:?0} }")//Prendi solo i clienti con il numero di bollini superiore (grater than) al parametro
	public ArrayList<Clienti> selByBollini(int Bollini);
	
}
