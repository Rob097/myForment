package com.multitenant.jwt.clients.services;

import java.util.ArrayList;

import com.multitenant.jwt.clients.models.Clienti;

public interface ClientiService {
	
	public ArrayList<Clienti> SetAll();
	
	public Clienti Salva(Clienti cliente);
	
	public Void Elimina(String Id);

	public Clienti SelByCodFid(String CodFid);
	
	public ArrayList<Clienti> SelByNominativo(String Nominativo);
	
	public ArrayList<Clienti> SelByBollini(int Bollini);
	
	public Void EliminaByCodFid(String Codfid);
}
