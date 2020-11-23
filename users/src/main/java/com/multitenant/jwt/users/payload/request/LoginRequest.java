package com.multitenant.jwt.users.payload.request;

import javax.validation.constraints.NotBlank;

/**
 * @author Roberto97 This is the entity used for the request of LOGIN. It allows
 *         to pass a username and a password as a single object
 */
public class LoginRequest {
	@NotBlank
	private String username;

	@NotBlank
	private String password;
	
	private boolean rememberMe;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}
}
