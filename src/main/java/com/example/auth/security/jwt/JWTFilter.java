package com.example.auth.security.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.auth.service.UserService;

@Component
public class JWTFilter extends OncePerRequestFilter {

	UserService service;
	JWTHelper jwtHelper;

	public JWTFilter(UserService service, JWTHelper jwtHelper) {
		this.service = service;
		this.jwtHelper = jwtHelper;
	}

	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {
		Optional<String> jwt = extractJWT(req);
		if (jwt.isPresent()) {
			try {
				String email = jwtHelper.validateTokenAndRetrieveEmail(jwt.get());
				UserDetails userDetails = service.loadUserByUsername(email);
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email,
						userDetails.getPassword(), userDetails.getAuthorities());
				if (SecurityContextHolder.getContext().getAuthentication() == null)
					SecurityContextHolder.getContext().setAuthentication(authToken);
			} catch (JWTVerificationException | EntityNotFoundException ex) {
			}
		}
		filterChain.doFilter(req, res);
	}

	private Optional<String> extractJWT(HttpServletRequest req) {
		if (req.getCookies() == null)
			return Optional.empty();
		return Arrays.stream(req.getCookies())
				.filter(c -> c.getName().equals("jwt"))
				.map(Cookie::getValue)
				.findAny();

	}

}
