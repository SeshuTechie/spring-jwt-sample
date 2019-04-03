package com.seshut.example.jwtauth.notes;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.seshut.example.jwtauth.user.User;

@Entity
public class Notes {

	@Id
	@GeneratedValue
	private int id;
	private String content;
	private String colorCode;
	private int impact;
	private long createdTime;
	
	@ManyToOne
	@Basic(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
}
