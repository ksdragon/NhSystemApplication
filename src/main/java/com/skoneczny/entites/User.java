package com.skoneczny.entites;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.skoneczny.annotation.ValidEmail;

;

@Entity
public class User {
	@Id
	@ValidEmail
	@NotEmpty
	@Column(unique = true)
	private String email;
	@NotEmpty
	@Size(min = 2, max = 30)
	private String name;
	@Size(min = 2, max = 30)
	private String lastName;
	@Size(min = 2, max = 30)
	private String street;
	@Size(min = 2, max = 30)
	private String numberHouse;
	@Size(min = 2, max = 30)
	private String city;
	@Size(min = 2, max = 30)
	private String zipCode;
	@Size(min = 2, max = 30)
	private String phone1;
	@Size(min = 2, max = 30)
	private String phone2;	
	private String birthday;	
	@Size(min = 4)
	private String password;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Task> tasks;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "USER_ROLES", joinColumns={
			@JoinColumn(name = "USER_EMAIL", referencedColumnName = "email") }, inverseJoinColumns = {
					@JoinColumn(name = "ROLE_NAME", referencedColumnName = "name") })
	private List<Role> roles;
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
	public List<Task> gettasks() {
		return tasks;
	}
	public void settasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getNumberHouse() {
		return numberHouse;
	}
	public void setNumberHouse(String numberHouse) {
		this.numberHouse = numberHouse;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	
	
	public User(@Email @NotEmpty String email, @NotEmpty @Size(min = 2, max = 30) String name,
			@Size(min = 2, max = 30) String lastName, @Size(min = 2, max = 30) String street,
			@Size(min = 2, max = 30) String numberHouse, @Size(min = 2, max = 30) String city,
			@Size(min = 2, max = 30) String zipCode, @Size(min = 2, max = 30) String phone1,
			@Size(min = 2, max = 30) String phone2, String birthday, @Size(min = 4) String password) {		
		this.email = email;
		this.name = name;
		this.lastName = lastName;
		this.street = street;
		this.numberHouse = numberHouse;
		this.city = city;
		this.zipCode = zipCode;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.birthday = birthday;
		this.password = password;
	}
	public User(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password = password;
	}
	public User() {
	}
}