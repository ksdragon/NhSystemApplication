package com.skoneczny.entites;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Role {
	@Id
	private String name;
	@ManyToMany(mappedBy = "roles", fetch=FetchType.EAGER)  //, cascade = CascadeType.ALL ,, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
	private List<User> users;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public Role(String name, List<User> users) {
		this.name = name;
		this.users = users;
	}
	public Role() {
	}
	public Role(String name) {
		this.name = name;
	}
		
}