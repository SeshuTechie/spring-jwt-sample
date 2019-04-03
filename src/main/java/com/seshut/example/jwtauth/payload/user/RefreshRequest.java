package com.seshut.example.jwtauth.payload.user;

import javax.validation.constraints.NotEmpty;

public class RefreshRequest {

	@NotEmpty
	private String refreshToken;

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
