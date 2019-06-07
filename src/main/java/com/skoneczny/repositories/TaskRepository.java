package com.skoneczny.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skoneczny.entites.Task;
import com.skoneczny.entites.User;

public interface TaskRepository extends JpaRepository<Task, Long>{

	List<Task> findByUser(User user, Sort sort);
	Page<Task> findByUser(User user, Pageable pageable);
	List<Task> findByUser(User user);
	List<Task> findByUserIn(List<User> user);
	
	@Query("select t from Task t where t.user = ?1 and startDate like ?2%" )
	Page<Task> findByUser(User user, String year, Pageable pageable);
}
