package com.seshut.example.jwtauth.payload.notes;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NotesRequest {

	@NotEmpty
	@Size(max=255)
	private String content;
	
	@NotNull
	@Min(1) 
	@Max(10)
	private Integer impact;

	private String colorCode;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getImpact() {
		return impact;
	}
	public void setImpact(Integer impact) {
		this.impact = impact;
	}
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	
}
