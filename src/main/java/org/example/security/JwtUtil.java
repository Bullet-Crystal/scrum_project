package org.example.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.example.model.RoleType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String SECRET;
	@Value("${jwt.expiration}")
	private long EXPIRATION;

	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET.getBytes());
	}

	public String generateToken(String username, Set<RoleType> roles) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("roles", roles.stream()
				.map(RoleType::name)
				.collect(Collectors.toList()));
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	public Set<RoleType> extractRoles(String token) {
		Claims claims = extractClaims(token);
		List<String> roleNames = claims.get("roles", List.class);

		if (roleNames == null || roleNames.isEmpty()) {
			return new HashSet<>();
		}

		return roleNames.stream()
				.map(RoleType::valueOf)
				.collect(Collectors.toSet());
	}

	public String extractUsername(String token) {
		return extractClaims(token).getSubject();
	}

	public boolean isTokenValid(String token, String username) {
		return extractUsername(token).equals(username) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractClaims(token).getExpiration().before(new Date());
	}

	private Claims extractClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
}
