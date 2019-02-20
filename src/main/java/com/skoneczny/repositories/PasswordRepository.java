package com.skoneczny.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skoneczny.entites.Password;

public interface PasswordRepository extends JpaRepository<Password, Long> {

}
