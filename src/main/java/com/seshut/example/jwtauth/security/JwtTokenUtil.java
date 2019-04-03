package com.seshut.example.jwtauth.security;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {

	private static final String SCOPES = "scopes";

	private static final String ROLE_REFRESH_TOKEN = "ROLE_REFRESH_TOKEN";
	
	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtTokenExpirationInMillis}")
	private int jwtExpiration;

	@Value("${app.refreshTokenExpirationInMillis}")
	private int refreshExpiration;

	public String generateToken(Authentication authentication) {

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		return generateToken(userDetails.getId());
	}

	public String generateToken(long id) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpiration);

		String jwt = Jwts.builder().setSubject(Long.toString(id)).setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
		return jwt;
	}

	public String generateRefreshToken(Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		return generateRefreshToken(userDetails.getId());
	}

	public String generateRefreshToken(long id) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + refreshExpiration);

		Claims claims = Jwts.claims().setSubject(Long.toString(id));
		claims.put(SCOPES, Arrays.asList(ROLE_REFRESH_TOKEN));

		String jwt = Jwts.builder().setClaims(claims).setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
		return jwt;
	}

	public Long getUserIdFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

		return Long.parseLong(claims.getSubject());
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public String refreshToken(String token) {
		
		Long userId = getUserIdFromToken(token);
		
		return generateToken(userId);
	}
	
	public boolean isRefreshToken(String token)
	{
		boolean flag = false;
		
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		List<String> scopes = (List<String>) claims.get(SCOPES);
		if(scopes != null && scopes.contains(ROLE_REFRESH_TOKEN))
		{
			flag = true;
		}
		return flag;
	}
}