package it.ifttt.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "userifttt")
public class User {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String username;
	//@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	@JsonIgnore
	private String salt;
	@JsonIgnore
	private boolean enabled;

	//@JsonIgnore
	/*@OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade=CascadeType.MERGE)
    private Set<Recipe> recipes = new HashSet<>();*/
	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade=CascadeType.ALL)
    private Set<Role> roles = new HashSet<>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/*
	@Access(AccessType.PROPERTY)
	@ManyToMany(fetch=FetchType.EAGER)
	public List<Recipe> getRecipes() {
		return recipes;
	}
	
	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}*/
	
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void addRole(Role role) {
		roles.add(role);
		role.setUser(this);
	}

}
