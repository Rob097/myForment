package com.myforment.users.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.myforment.users.security.jwt.AuthEntryPointJwt;
import com.myforment.users.security.jwt.AuthTokenFilter;
import com.myforment.users.services.UserDetailsServiceImpl;

/**
 * @author Roberto97
 * This is the most important class to configure and manage the Spring Security and the JWT
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	/**
	 * USER_MATCHER: Path where you need to have the role of user to access at.
	 * ADMIN_MATCHER: Path where you need to have the role of admin to access at.
	 * MOD_MATCHER: Path where you need to have the moderator of user to access at.
	 */
	private static final String[] BASIC_MATCHER = { "/api/clienti/cerca/**", "/api/companies/**"};
	private static final String[] ADMIN_MATCHER = { "/api/clienti/inserisci/**", "/api/clienti/elimina/**", "/api/auth/getAll" };
	private static final String[] EDITOR_MATCHER = {};
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * To crypt the password bCrypt is used.
	 * @return bCrypt password encoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
	/**
	 * Principal method of configuration for this class.
	 * .cors() is needed to allow the application to manage the CORS POLICY errors.
	 * The method is easy to understand if you red it.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
			.cors().and()
			.csrf().disable()
			.formLogin().disable()
			.httpBasic().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()			
			.authorizeRequests()		
			//.anyRequest().permitAll();
			.antMatchers("/*.css", "/*.png", "/*.js", "/*.json", "/*.ico").permitAll()
			.antMatchers("/static/**").permitAll()//Molto importante per il primo caricamento
			.antMatchers(HttpMethod.OPTIONS).permitAll()
			.antMatchers("/api/auth/**").permitAll()
			.antMatchers("/api/test/**").permitAll()
			.antMatchers(BASIC_MATCHER).hasAnyRole(Properties.ROLE_BASIC)
			.antMatchers(ADMIN_MATCHER).hasAnyRole(Properties.ROLE_ADMIN)
			.antMatchers(EDITOR_MATCHER).hasAnyRole(Properties.ROLE_EDITOR)
			.anyRequest().authenticated();
			
		
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		
		// disable page caching
		http.headers().cacheControl();
	}
}
