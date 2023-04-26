package com.example.auth.security.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JWTHelper {
	@Value("jwt-secret")
	private String secret;

	public String generateToken(String email) {
		return JWT.create()
				.withSubject("User Details")
				.withClaim("email", email)
				.withIssuedAt(new Date())
				.withIssuer("Corporation Corporation")
				.sign(Algorithm.HMAC256(secret));
	}

	public String validateTokenAndRetrieveEmail(String token) {
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
				.withSubject("User Details")
				.withIssuer("Corporation Corporation")
				.build();
		DecodedJWT jwt = verifier.verify(token);
		return jwt.getClaim("email").asString();
	}

}
