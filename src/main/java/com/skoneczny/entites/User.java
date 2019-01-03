package com.skoneczny.entites;


import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
	@Temporal(TemporalType.DATE)
	private Date birthday;	
	@Size(min = 4)
	private String password;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Address> addresses;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Phone> phones;
	
	// Activate a New Account by Email - add field indicate that account is enable
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Task> tasks;
	/**
	 * kaskadowość(cascade = {CascadeType.PERSIST itp) - to operacje na obiektach powiązanych w jakiejś relacji 
	 * jak ustawimy tą adnotację nie potrzebujemy w jednej tansakcji zapisywać po kolej poszczególnych obiektów
	 * tylko ten jeden który nas interesuje.
	 * 	 * 
	 * fetch.eager - pobiera obiekt w relecji 1-* odrazu np. jeżeli pobierzemy użytkownika to 
	 * odrazu zrobi selecta z outer join do tabeli USER_ROLES
	 * domyślnie jest fetch.lazy który pobierze tylko obiekt user bez żedanych powiązań a
	 * hibernet jak będzie potrzebował to zrobi takie dopytanie. 
	 */
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
	
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}	
	
		
	public List<Address> getAdress() {
		return addresses;
	}
	public void setAdress(List<Address> address) {
		this.addresses = address;
	}
	public List<Phone> getPhones() {
		return phones;
	}
	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
		result = prime * result + ((addresses == null) ? 0 : addresses.hashCode());
		result = prime * result + ((birthday == null) ? 0 : birthday.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((phones == null) ? 0 : phones.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + ((tasks == null) ? 0 : tasks.hashCode());
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
		if (addresses == null) {
			if (other.addresses != null)
				return false;
		} else if (!addresses.equals(other.addresses))
			return false;
		if (birthday == null) {
			if (other.birthday != null)
				return false;
		} else if (!birthday.equals(other.birthday))
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
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (phones == null) {
			if (other.phones != null)
				return false;
		} else if (!phones.equals(other.phones))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		if (tasks == null) {
			if (other.tasks != null)
				return false;
		} else if (!tasks.equals(other.tasks))
			return false;
		return true;
	}
	
	
	
	
	
	
	
	
	
	
}