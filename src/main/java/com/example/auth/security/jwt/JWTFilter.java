package com.example.auth.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTDecodeException;
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
		String header = req.getHeader("Authorization");
		if (header != null && !header.isBlank() && header.startsWith("Bearer ")) {
			String jwt = extractJWT(header);

			String email = jwtHelper.validateTokenAndRetrieveEmail(jwt);
			UserDetails userDetails = service.loadUserByUsername(email);

			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email,
					userDetails.getPassword(), userDetails.getAuthorities());
			if (SecurityContextHolder.getContext().getAuthentication() == null)
				SecurityContextHolder.getContext().setAuthentication(authToken);
		}
		filterChain.doFilter(req, res);
	}

	private String extractJWT(String header) {
		if (header != null && !header.isBlank() && header.startsWith("Bearer ")) {
			String jwt = header.substring(7);
			if (jwt == null || jwt.isBlank())
				throw new JWTDecodeException("Blank token in header.");
			return jwt;
		}
		throw new JWTDecodeException("Invalid authorization header");
	}

}
