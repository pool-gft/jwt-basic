package com.example.auth.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.auth.security.jwt.JWTFilter;
import com.example.auth.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserService service;
	@Autowired
	private JWTFilter filter;
	// @Autowired
	// @Qualifier("delegatedAuthenticationEntryPoint")
	// AuthenticationEntryPoint delegatedAuthExceptionHandler;

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		return http
				.httpBasic(httpBasic -> httpBasic.disable())
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(r -> r
						.antMatchers("/auth/**").permitAll()
						.antMatchers("/h2/**").permitAll()
						.antMatchers("/users/info").hasRole("USER"))
				.userDetailsService(service)
				.exceptionHandling(handler -> handler.authenticationEntryPoint(
						(req, res, ex) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
				.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

}
//
// @Component("delegatedAuthenticationEntryPoint")
// class DelegatedAuthneticationEntryPoint implements AuthenticationEntryPoint {
// @Autowired
// @Qualifier("handlerExceptionResolver")
// private HandlerExceptionResolver resolver;
//
// public void commence(HttpServletRequest req, HttpServletResponse res,
// AuthenticationException ex) {
// resolver.resolveException(req, res, null, ex);
// }
//
// }