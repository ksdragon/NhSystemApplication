package com.skoneczny.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.skoneczny.entites.User;

public interface UserRepository extends JpaRepository<User, String> {

	List<User> findByNameLike(String name, Pageable pageable);
	List<User> findByNameLike(String name, Sort sort);
	

}
