package com.seshut.example.jwtauth.payload.user;

public class RefreshTokenResponse {

	private String accessToken;
	
	public RefreshTokenResponse(String accessToken) {
		super();
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
