package com.multitenant.jwt.users.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.multitenant.jwt.users.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Roberto97
 * Provides core user information.
 */
public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private String id;

	private String username;

	private String email;

	@JsonIgnore
	private String password;
	
	private String tenantId;

	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(String id, String username, String email, String password, String tenantId,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.tenantId = tenantId;
		this.authorities = authorities;
	}

	public static UserDetailsImpl build(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());

		return new UserDetailsImpl(
				user.getId(), 
				user.getUsername(), 
				user.getEmail(),
				user.getPassword(), 
				user.getTenantId(),
				authorities);
	}

	/**
	 * Returns the authorities granted to the user. Cannot return null.
	 * @return the authorities, sorted by natural key (never null)
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	/**
	 * Returns the id used for the user.
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the email used for the user.
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Returns the password used to authenticate the user.
	 * @return the password
	 */
	@Override
	public String getPassword() {
		return password;
	}
	
	
	/**
	 * Returns the tenant id assigned to the user.
	 * @return the tenant id
	 */
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * Returns the username used to authenticate the user. Cannot return null.
	 * @return the password
	 */
	@Override
	public String getUsername() {
		return username;
	}

	/**
	 * Indicates whether the user's account has expired. An expired account cannot be authenticated.
	 * @return true if the user's account is valid (ie non-expired), false if no longer valid (ie expired)
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * Indicates whether the user is locked or unlocked. A locked user cannot be authenticated.
	 * @return true if the user is not locked, false otherwise
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * Indicates whether the user's credentials (password) has expired. Expired credentials prevent authentication.
	 * @return true if the user's credentials are valid (ie non-expired), false if no longer valid (ie expired)
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * Indicates whether the user is enabled or disabled. A disabled user cannot be authenticated.
	 * @return true if the user is enabled, false otherwise
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}
}
