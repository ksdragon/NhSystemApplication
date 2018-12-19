package com.skoneczny.entites;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.transaction.annotation.Transactional;

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
	
	// Activate a New Account by Email - add field indicate that account is enable
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Task> tasks;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch=FetchType.EAGER)  //cascade = CascadeType.ALL
	@JoinTable(name = "USER_ROLES", 
			joinColumns={@JoinColumn(name = "USER_EMAIL", referencedColumnName = "email") },
			inverseJoinColumns = {@JoinColumn(name = "ROLE_NAME", referencedColumnName = "name") })
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
	//@Transactional
	public void removeUser(Role r) {
		roles.remove(r);
		r.getUsers().remove(this);
		}
		
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public User(@Email @NotEmpty String email, @NotEmpty @Size(min = 2, max = 30) String name,
			@Size(min = 2, max = 30) String lastName, @Size(min = 2, max = 30) String street,
			@Size(min = 2, max = 30) String numberHouse, @Size(min = 2, max = 30) String city,
			@Size(min = 2, max = 30) String zipCode, @Size(min = 2, max = 30) String phone1,
			@Size(min = 2, max = 30) String phone2, String birthday, @Size(min = 4) String password, Boolean isEnabled) {		
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
		this.enabled = isEnabled;
	}
	public User(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password = password;
	}
	public User() {
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((birthday == null) ? 0 : birthday.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((numberHouse == null) ? 0 : numberHouse.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((phone1 == null) ? 0 : phone1.hashCode());
		result = prime * result + ((phone2 == null) ? 0 : phone2.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((tasks == null) ? 0 : tasks.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (birthday == null) {
			if (other.birthday != null)
				return false;
		} else if (!birthday.equals(other.birthday))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (enabled == null) {
			if (other.enabled != null)
				return false;
		} else if (!enabled.equals(other.enabled))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (numberHouse == null) {
			if (other.numberHouse != null)
				return false;
		} else if (!numberHouse.equals(other.numberHouse))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (phone1 == null) {
			if (other.phone1 != null)
				return false;
		} else if (!phone1.equals(other.phone1))
			return false;
		if (phone2 == null) {
			if (other.phone2 != null)
				return false;
		} else if (!phone2.equals(other.phone2))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (tasks == null) {
			if (other.tasks != null)
				return false;
		} else if (!tasks.equals(other.tasks))
			return false;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		return true;
	}
	
	
	
	
}