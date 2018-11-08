package com.skoneczny.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skoneczny.entites.User;

public interface UserRepository extends JpaRepository<User, String> {

	List<User> findByNameLike(String name);

}
