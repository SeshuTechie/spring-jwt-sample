package com.seshut.example.jwtauth.payload.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class SignupRequest {
	
	@NotEmpty
	@Email
	private String email;
	
	@NotEmpty
	private String name;

	@NotEmpty
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
