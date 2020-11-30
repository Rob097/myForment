package com.myforment.clients.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.myforment.clients.models.Clienti;
import com.myforment.clients.repository.ClientiRepository;
import com.myforment.users.multitenant.MongoTemplateCustom;

@Service
public class ClientiServiceImpl implements ClientiService{
	
	//Collego lo stato di persistenza
	@Autowired
	ClientiRepository clientiRepository;
	
	@Autowired
	@Qualifier("utentiTemplate")
	private MongoTemplateCustom utentiTemplate;
	
	
	
	//============================================================================================================================
	
	@Override
	public ArrayList<Clienti> SetAll() {
		
		return new ArrayList<Clienti>(utentiTemplate.findAll(Clienti.class));
		
	}
	
	//============================================================================================================================

	@Override
	public Clienti Salva(Clienti cliente) {

		//return clientiRepository.insert(cliente); insert permette solo di inserrire ma non di aggiornare
		return utentiTemplate.save(cliente);
		
	}
	
	//============================================================================================================================

	@Override
	public Void Elimina(String Id) {

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(Id));
		utentiTemplate.remove(query, Clienti.class);
		return null;

	}
	
	//============================================================================================================================

	@Override
	public Clienti SelByCodFid(String CodFid) {

		Query query = new Query();
		query.addCriteria(Criteria.where("codfid").is(CodFid));
		List<Clienti> c = utentiTemplate.find(query, Clienti.class);
		if(c.isEmpty())
			return null;
		else
			return c.get(0);
		
	}
	
	//============================================================================================================================

	@Override
	public ArrayList<Clienti> SelByNominativo(String Nominativo) {

		Query query = new Query();
		query.addCriteria(Criteria.where("nominativo").is(Nominativo));
		return (ArrayList<Clienti>) utentiTemplate.find(query, Clienti.class);
		
	}
	
	//============================================================================================================================

	@Override
	public ArrayList<Clienti> SelByBollini(int Bollini) {

		return clientiRepository.selByBollini(Bollini);
		
	}
	
	//============================================================================================================================

	@Override
	public Void EliminaByCodFid(String Codfid) {

		Clienti c = clientiRepository.findByCodfid(Codfid);
		if(c != null) {
			clientiRepository.deleteById(c.getId());
		}
		return null;
		
	}
	
	//============================================================================================================================

}
