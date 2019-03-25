package com.skoneczny.entites;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.lang.NonNull;

 
 @Entity
public class Task {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;	
	@NotEmpty
	@NonNull
	private String startDate;
	@NotEmpty
	@Column(nullable = false)
	private String startTime;
	@NotEmpty
	@Column(nullable = false)
	private String duration;
//	@NotEmpty
//	@Column(nullable = false)
	private String stopDate;
//	@NotEmpty
//	@Column(nullable = false)
	private String stopTime;	
	@Column(length=1000)
	private String description;	
	@Column(name = "approved", nullable = false)
	private Boolean isApproved = false;
	@ManyToOne
	@JoinColumn(name="USER_EMAIL")
	private User user;	
	@ManyToOne
	@NotNull
	@JoinColumn(name = "id_taskCategory", nullable = false)
	private CategoryTask categoryTasks;	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String date) {
		this.startDate = date;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getStopTime() {
		return stopTime;
	}
	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}	
	
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getStopDate() {
		return stopDate;
	}
	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}
	public Boolean getIsApproved() {
		return isApproved;
	}
	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}
	public CategoryTask getCategoryTasks() {
		return categoryTasks;
	}
	public void setCategoryTasks(CategoryTask categoryTasks) {
		this.categoryTasks = categoryTasks;
	}
	
	
	
	public Task(Long id, @NotEmpty String startDate, @NotEmpty String startTime, @NotEmpty String duration,
			@NotEmpty String stopDate, @NotEmpty String stopTime, String description, Boolean isApproved, User user,
			CategoryTask categoryTasks) {		
		this.id = id;
		this.startDate = startDate;
		this.startTime = startTime;
		this.duration = duration;
		this.stopDate = stopDate;
		this.stopTime = stopTime;
		this.description = description;
		this.isApproved = isApproved;
		this.user = user;
		this.categoryTasks = categoryTasks;
	}
	public Task(String startDate, String startTime, String stopTime, String description) {
		this.startDate = startDate;
		this.startTime = startTime;
		this.stopTime = stopTime;
		this.description = description;
	}
	public Task() {
	}
	
	
	
	
 }