package com.bank.Banking_Application.config;

import java.security.Key;
import java.util.Base64.Decoder;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtTokenProvider {

	@Value("${app.jwt-secret}")
	private String jwtSecret;

	@Value("${app.jwt-expiration}")
	private long JwtExpirationDate;

	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime() + JwtExpirationDate);
		return Jwts.builder().subject(username).issuedAt(currentDate).expiration(expireDate).signWith(key()).compact();

	}

	private Key key() {
		byte[] bytes = Decoders.BASE64.decode(jwtSecret);
		return Keys.hmacShaKeyFor(bytes);
	}

	public String getUsername(String token) {
		Claims claims = Jwts.parser().setSigningKey(key())
				// .verifyWith((SecretKey) key())
				.build().parseClaimsJws(token)
				// .parseSignedClaims(token)
				// .getPayload();
				.getBody();
		return claims.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(key()).build().parse(token);
			return true;

		} catch (ExpiredJwtException e) {
			throw new RuntimeException();
		} catch (MalformedJwtException e) {
			throw new RuntimeException();
		} catch (SignatureException e) {
			throw new RuntimeException();
		} catch (IllegalArgumentException e) {
			throw new RuntimeException();
		}

	}
}
