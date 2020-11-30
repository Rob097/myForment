package com.myforment.users.security.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.myforment.users.multitenant.MongoTemplateCustom;
import com.myforment.users.security.configuration.Properties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * @author Roberto97
 * This filter has the really important purpose of intercept every request that arrives in the backend part of the application to 
 * verify the validity of the token and set as authenticated the request, and as final step, responsing with the UserDetails with the claims arrived with the request
 */
public class AuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private Properties properties;
	
	@Autowired
	@Qualifier("utentiTemplate")
	private MongoTemplateCustom utentiTemplate;
	
	private final String idAuthorities = Properties.Id_AUTHORITIES;
	
	private final String USER_ID_ATTRIBUTE = Properties.USER_ID_ATTRIBUTE;
	private final String CLAIM_KEY_AUTHORITIES = Properties.CLAIM_KEY_AUTHORITIES;

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	/**
	 * Se il token viene giudicato valido viene impostato nel contesto della request
	 * l’autenticazione con relativo principal e viene invocata la chain.doFilter la
	 * quale farà scattare i filtri di Spring Security. If the token is considered
	 * as valid, the authentication is setted inside the request. Then the
	 * filterChain.doFilter is invoked.
	 * 
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = parseJwt(request);

			if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
				
				String userId = jwtUtils.getClaim(jwt, USER_ID_ATTRIBUTE, String.class);
				
				//Se il tenantId recuperato dai claims del token è valido, lo imposto come default user id e database del template user di mongo.
				//L'attributo viene utilizzato dalla MultitenantMongoDbFactory per trovare il giusto database.
				if(userId != null) {

					//Important! Setting default user database.
					utentiTemplate.setDefaultUserDb(userId);

				}
				
				UsernamePasswordAuthenticationToken authentication = getAuthentication(request, jwt);
				
				SecurityContextHolder.getContext().setAuthentication(authentication);

			}
		} catch (Exception e) {
			logger.error("Cannot set user authentication: {}", e);
		}

		filterChain.doFilter(request, response);
	}
	
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, String jwt) {
        String token = request.getHeader(properties.getTokenHeader());
        if (token != null) {
            // parse the token.
            String user = Jwts.parser().setSigningKey(properties.getJwtSecret()).parseClaimsJws(jwt).getBody().getSubject();

            if (user != null) {
            	
            	Claims claims = jwtUtils.getAllClaimsFromToken(jwt);
            	
            	Collection<? extends GrantedAuthority> authorities;            	
            	
            	Collection<?> roles = claims.get(CLAIM_KEY_AUTHORITIES, Collection.class);
            	
            	
            	if (null != roles) {
                    ArrayList<GrantedAuthority> authsList = new ArrayList<>(roles.size());
                    
                    for (Object role : roles) {
                    	
                    	@SuppressWarnings("unchecked")
                    	HashMap<String, String> roleName = (HashMap<String, String>) role;

                        authsList.add(new SimpleGrantedAuthority(roleName.get(idAuthorities).toString()));
                    }
                    
                    authorities = Collections.unmodifiableList(authsList);

                } else {
                    authorities = Collections.emptyList();
                }
            	
                return new UsernamePasswordAuthenticationToken(user, null, authorities);
            }
            return null;
        }
        return null;
    }

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader(properties.getTokenHeader());

		if (headerAuth != null && StringUtils.hasText(headerAuth) && headerAuth.startsWith(properties.getTokenConstant()) && headerAuth.length() > 7) {
			return headerAuth.substring(7, headerAuth.length());
		}else {			
			return null;
		}
		
	}
}
