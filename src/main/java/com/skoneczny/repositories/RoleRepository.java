package com.skoneczny.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skoneczny.entites.Role;

public interface RoleRepository extends JpaRepository<Role, String>{
	

}
