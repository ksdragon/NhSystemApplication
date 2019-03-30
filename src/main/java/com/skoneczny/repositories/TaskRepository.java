package com.skoneczny.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.skoneczny.entites.Task;
import com.skoneczny.entites.User;

public interface TaskRepository extends JpaRepository<Task, Long>, PagingAndSortingRepository<Task,Long>{

	List<Task> findByUser(User user, Sort sort);
	List<Task> findByUser(User user, Pageable pageable);
	List<Task> findByUser(User user);

}
