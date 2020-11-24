package com.myforment.users.security.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.myforment.users.security.configuration.Properties;
import com.myforment.users.services.UserDetailsImpl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClock;

/**
 * @author Roberto97 This class build the token saving into the payload of it
 *         the most useful informations that are inside our implementation of
 *         userDetails and setting an expiring date equals to the property
 *         jwtExpirationMs
 */
@Component
public class JwtUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);


	public static final String CLAIM_KEY_AUTHORITIES = Properties.CLAIM_KEY_AUTHORITIES;
	public static final String USER_ID_ATTRIBUTE = Properties.USER_ID_ATTRIBUTE;

	private static Clock clock = DefaultClock.INSTANCE;

	/**
	 * @param authentication
	 * @param rememberMe 
	 * @param userId 
	 * @return the token as a String
	 * Method used for the initial generation of the JWT token. 
	 */
	public String generateJwtToken(Authentication authentication, boolean rememberMe, String userId) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		long exp = 0;

		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_AUTHORITIES, userPrincipal.getAuthorities());
		
		if(userId != null)
			claims.put(USER_ID_ATTRIBUTE, userId);
		
		if(rememberMe) {
			exp = Properties.jwtExpirationMsRememberMe;
		}else {
			exp = Properties.jwtExpirationMs;
		}

		return Jwts.builder().setClaims(claims).setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + exp))
				.signWith(SignatureAlgorithm.HS512, Properties.jwtSecret).compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(Properties.jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(Properties.jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}

	Claims getAllClaimsFromToken(String token) {
		try {
			Claims claims = Jwts.parser().setSigningKey(Properties.jwtSecret).parseClaimsJws(token).getBody();
			return claims;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		} catch (Exception e) {
			logger.error("Errore Generico: {}", e.getMessage());
		}
		return null;
	}
	
	public String getClaim(String token, String name, Class<?> requiredType) {
		Claims claims = this.getAllClaimsFromToken(token);
		return claims.get(name, requiredType).toString();
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		try {
			final Claims claims = getAllClaimsFromToken(token);
			return claimsResolver.apply(claims);
		} catch (Exception e) {
			throw e;
		}
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(clock.now());
	}

	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	private Date calculateExpirationDate(Date createdDate, boolean rememberMe) {
		
		if (rememberMe) {
			return new Date(createdDate.getTime() + Properties.jwtExpirationMsRememberMe);
		}else {
			System.out.println("check: " + rememberMe);
			return new Date(createdDate.getTime() + Properties.jwtExpirationMs);
		}
		
	}

	/**
	 * @param token
	 * @param rememberMe it's used to set the expiration date to 30 days if it's true and to 2 hours instead
	 * @return the token as a String
	 * Method used to generate a refreshed JWT token. 
	 */
	public String refreshToken(String token, boolean rememberMe) {
		final Date createdDate = clock.now();
		final Date expirationDate = calculateExpirationDate(createdDate, rememberMe);

		final Claims claims = getAllClaimsFromToken(token);
		claims.setIssuedAt(createdDate);
		claims.setExpiration(expirationDate);

		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, Properties.jwtSecret).compact();
	}
}
