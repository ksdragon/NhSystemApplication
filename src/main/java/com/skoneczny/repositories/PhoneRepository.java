package com.skoneczny.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skoneczny.entites.Phone;
import com.skoneczny.entites.User;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
	
	List<Phone> findByUser(User user);

}
