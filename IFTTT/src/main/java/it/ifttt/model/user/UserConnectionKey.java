package it.ifttt.model.user;

import java.io.Serializable;

public class UserConnectionKey implements Serializable {

	private static final long serialVersionUID = 2096443226601711146L;
	
	//private User user;
	private String userId;
	private String providerId;
	private String providerUserId;
	/*
	public User getUser() {
		return user;
	}
	public void setUserId(User user) {
		this.user = user;
	}
	*/
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	public String getProviderUserId() {
		return providerUserId;
	}
	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}
	
	
}
