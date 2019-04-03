package com.seshut.example.jwtauth.exception;

public class ExceptionDetails {

	private String errorMessage;
	private String details;
	
	public ExceptionDetails(String errorMessage, String details) {
		super();
		this.errorMessage = errorMessage;
		this.details = details;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
}
