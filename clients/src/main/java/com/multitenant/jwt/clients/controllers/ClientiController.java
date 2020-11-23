package com.multitenant.jwt.clients.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.multitenant.jwt.clients.models.Clienti;
import com.multitenant.jwt.clients.models.MessageResponse;
import com.multitenant.jwt.clients.services.ClientiService;

/**
 * @author Roberto97
 *Controller of Clients.
 * All the methods in this controller are under the path "/api/clienti" and in particular each one has it's relative path.
 * All the methods are also with a different http method between GET, POS and DELETE.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/clienti")
public class ClientiController {

	@Autowired
	ClientiService clientiService;
	
	@GetMapping(value="/auth", produces="application/json")
	public String checkBasicAuth(){
		return "Autenticazione OK";
	}
	
	
	/**
	 * Method used to inserrt a client in the DB.
	 * It get, by post request, the client object and thanks to the Salva method of the clientiService, it add the client to the DB
	 * @param NewCliente Oggetto cliente creato nella form react
	 * @return Entity of Clienti type and CREATED status
	 */
	@PostMapping(value= "/inserisci", produces = "application/json")
	public ResponseEntity<Clienti> insertCli(@RequestBody Clienti NewCliente){
		
		Clienti cliente = clientiService.Salva(NewCliente);
		
		return new ResponseEntity<Clienti>(cliente, HttpStatus.CREATED);
		
	}
	
	@RequestMapping(value= "/cerca/all", method= RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> listAllCli(){
		
		ArrayList<Clienti> clienti = clientiService.SetAll();
		if(clienti == null || clienti.isEmpty()) {
			return ResponseEntity.badRequest().body(new MessageResponse("Non è stato trovato alcun cliente!"));
		}
		
		return new ResponseEntity<ArrayList<Clienti>>(clienti, HttpStatus.OK);
		
	}
	
	@DeleteMapping(value="/elimina/{id}", produces="application/json")
	public ResponseEntity<Void> deleteCli(@PathVariable("id") String Id){
		
		return new ResponseEntity<Void>(clientiService.Elimina(Id), HttpStatus.OK);
		
	}
	
	@RequestMapping(value= "/cerca/codice/{codfid}", method= RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getCliByCode(@PathVariable("codfid") String CodFid){
		
		Clienti cliente = clientiService.SelByCodFid(CodFid);
		if(cliente == null) {
			return ResponseEntity.badRequest().body(new MessageResponse("Non è stato trovato alcun cliente con questo Codice Fidelity!"));
		}
		
		return new ResponseEntity<Clienti>(cliente, HttpStatus.OK);
		
	}
	
	@GetMapping(value= "/cerca/nominativo/{filter}",  produces = "application/json")
	public ResponseEntity<?> getCliByNom(@PathVariable("filter") String Nominativo){
		
		ArrayList<Clienti> clienti = clientiService.SelByNominativo(Nominativo);
		if(clienti == null || clienti.isEmpty()) {
			return ResponseEntity.badRequest().body(new MessageResponse("Non è stato trovato alcun cliente con questo Nominativo!"));
		}

		return new ResponseEntity<ArrayList<Clienti>>(clienti, HttpStatus.OK);
		
	}
	
	@GetMapping(value= "/cerca/bollini/{filter}", produces = "application/json")
	public ResponseEntity<?> getCliByBol(@PathVariable("filter") int Bollini){
		
		ArrayList<Clienti> clienti = clientiService.SelByBollini(Bollini);
		if(clienti == null || clienti.isEmpty()) {
			return ResponseEntity.badRequest().body(new MessageResponse("Non è stato trovato alcun cliente con più di \"+Bollini+\" bollini!"));
		}

		return new ResponseEntity<ArrayList<Clienti>>(clienti, HttpStatus.OK);
		
	}
	
	@DeleteMapping(value="/elimina/codfid/{codfid}", produces="application/json")
	public ResponseEntity<?> deleteCliByCodFid(@PathVariable("codfid") String CodFid){
		
		System.out.println("Codice: " + CodFid);
	
		Void eliminato;
		
		if(CodFid == null || CodFid.length() == 0 || CodFid.equals("null")) {
			return ResponseEntity.badRequest().body(new MessageResponse("Il codice fidelity che si vuole eliminare non esiste o non è valido"));
		}else {
			eliminato = clientiService.EliminaByCodFid(CodFid);
		}

		return new ResponseEntity<Void>(eliminato, HttpStatus.OK);

		
	}
	
}
