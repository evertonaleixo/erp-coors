package br.com.lima.erpcoors.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "users")
public class User {

	@Id
	private String username;
	private boolean enabled;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
