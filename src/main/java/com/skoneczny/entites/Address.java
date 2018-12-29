package com.skoneczny.entites;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

@Entity
public class Address {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	@Size(min = 2, max = 30)
	private String street;
	@Size(min = 2, max = 30)
	private String numberHouse;
	@Size(min = 2, max = 30)
	private String city;
	@Size(min = 2, max = 30)
	private String zipCode;
	@Size(min = 2, max = 30)
	private String country;
	
	
	@ManyToOne
	@JoinColumn(name="USER_EMAIL")
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	
	
	
}
