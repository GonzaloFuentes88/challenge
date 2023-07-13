package com.gonzalo.challenge.techforb.security;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class TokenUtils {

	private final static String ACCESS_TOKEN_SECRET ="eyJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6ImFkbWluIiwidXNlcm5hbWUiOiJhZG1pbiJ9.dl2B-xn6vpw1hR2DAxDj-1paKXGm79CAp1BFtPIjPxU";
	private final static Long ACCESS_TOKEN_VALIDITY_SECONDS = 1L;
	
	public static String createToken(String id, String username) {
		Long expirationTime = ACCESS_TOKEN_VALIDITY_SECONDS * 1000;
		Date expirationDate = new Date(System.currentTimeMillis() * expirationTime); 
		
		Map<String, Object> extra = new HashMap<>();
		
		extra.put("id", id);
		
		return Jwts
				.builder()
				.setSubject(username)
				.setExpiration(expirationDate)
				.addClaims(extra)
				.signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
				.compact();
	}
	
	public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
		
		try {
			Claims claims = Jwts
					.parserBuilder()
					.setSigningKey(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
					.build()
					.parseClaimsJws(token)
					.getBody();
			
			String email = claims.getSubject();
			
			return new UsernamePasswordAuthenticationToken(email, null,Collections.emptyList());			
		}catch (JwtException e) {
			return null;
		}
		
	}
	
}
