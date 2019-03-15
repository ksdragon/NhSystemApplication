package com.skoneczny.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skoneczny.entites.CategoryTask;

public interface CategoryTaskRepository extends JpaRepository<CategoryTask, Long>{
	
}
