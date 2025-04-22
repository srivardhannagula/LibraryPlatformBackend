package com.libraryproject.main.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import com.libraryproject.main.entity.Role;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
//UTIL IS USED TO GENERATE THE TOKEN AND VALIDATE THE TOKEN
@Component
public class JwtUtil {
	private String SECRET="mysecretkeyformyjwtmustbeatleast256bitslong";
	 private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

	public String generateToken(String userrname,Role role) {
		return Jwts.builder()
				.setSubject(userrname)
				.claim("role", role.name())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
				.signWith(key,SignatureAlgorithm.HS256)
				.compact();
	}
	public String extractUsername(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody()
				.getSubject();
	}
	
}
