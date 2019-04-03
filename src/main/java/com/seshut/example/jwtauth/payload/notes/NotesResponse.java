package com.seshut.example.jwtauth.payload.notes;

public class NotesResponse {
	private String id;
	private String content;
	private int impact;
	private String colorCode;
	private long createdTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getImpact() {
		return impact;
	}
	public void setImpact(int impact) {
		this.impact = impact;
	}
	public long getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
}
