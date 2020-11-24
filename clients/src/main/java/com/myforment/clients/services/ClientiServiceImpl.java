package com.myforment.clients.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myforment.clients.models.Clienti;
import com.myforment.clients.repository.ClientiRepository;

@Service
public class ClientiServiceImpl implements ClientiService{

	//Collego lo stato di persistenza
	@Autowired
	ClientiRepository clientiRepository;
	
	
	@Override
	public ArrayList<Clienti> SetAll() {

		return new ArrayList<Clienti>(clientiRepository.findAll());
		
	}

	@Override
	public Clienti Salva(Clienti cliente) {

		//return clientiRepository.insert(cliente); insert permette solo di inserrire ma non di aggiornare
		return clientiRepository.save(cliente);
		
	}

	@Override
	public Void Elimina(String Id) {

		clientiRepository.deleteById(Id);
		return null;

	}

	@Override
	public Clienti SelByCodFid(String CodFid) {

		return clientiRepository.findByCodfid(CodFid);
		
	}

	@Override
	public ArrayList<Clienti> SelByNominativo(String Nominativo) {

		return clientiRepository.findByNominativoLike(Nominativo);
		
	}

	@Override
	public ArrayList<Clienti> SelByBollini(int Bollini) {

		return clientiRepository.selByBollini(Bollini);
		
	}

	@Override
	public Void EliminaByCodFid(String Codfid) {

		Clienti c = clientiRepository.findByCodfid(Codfid);
		if(c != null) {
			clientiRepository.deleteById(c.getId());
		}
		return null;
		
	}

}
