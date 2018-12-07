package com.skoneczny.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skoneczny.entites.User;
import com.skoneczny.entites.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

	VerificationToken findByToken(String token);
	 
    VerificationToken findByUser(User user);
}
