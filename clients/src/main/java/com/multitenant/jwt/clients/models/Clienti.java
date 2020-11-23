package com.multitenant.jwt.clients.models;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Clienti implements Serializable {

	private static final long serialVersionUID = 4919829532823449378L;

	@Id
	private String id;

	@Field("codfid")
	private String codfid;

	@Field("nominativo")
	private String nominativo;

	@Field("indirizzo")
	private String indirizzo;

	@Field("comune")
	private String comune;

	@Field("cap")
	private String cap;

	@Field("prov")
	private String prov;

	@Field("telefono")
	private String telefono;

	@Field("mail")
	private String mail;

	@Field("attivo")
	private Boolean attivo;

	@Field("datacreazione")
	private Date datacreazione = new Date();

	@Field("cards")
	private Cards cards;
}
