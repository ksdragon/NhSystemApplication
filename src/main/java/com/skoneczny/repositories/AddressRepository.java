package com.skoneczny.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skoneczny.entites.Address;
import com.skoneczny.entites.User;

public interface AddressRepository  extends JpaRepository<Address, Long>{
	
	List<Address> findByUser(User user);

}
