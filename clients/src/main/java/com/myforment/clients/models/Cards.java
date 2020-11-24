package com.myforment.clients.models;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cards implements Serializable {

	private static final long serialVersionUID = -1494933990511240460L;

	@Field("bollini")
	private int bollini;

	@Field("ultimaspesa")
	private String ultimaspesa;

}
