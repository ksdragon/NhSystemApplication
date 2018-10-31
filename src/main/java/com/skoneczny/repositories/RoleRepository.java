package com.skoneczny.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skoneczny.entites.Role;

public interface RoleRepository extends JpaRepository<Role, String>{

}
