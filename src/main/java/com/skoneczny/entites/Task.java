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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoryTasks == null) ? 0 : categoryTasks.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isApproved == null) ? 0 : isApproved.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((stopDate == null) ? 0 : stopDate.hashCode());
		result = prime * result + ((stopTime == null) ? 0 : stopTime.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (categoryTasks == null) {
			if (other.categoryTasks != null)
				return false;
		} else if (!categoryTasks.equals(other.categoryTasks))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isApproved == null) {
			if (other.isApproved != null)
				return false;
		} else if (!isApproved.equals(other.isApproved))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (stopDate == null) {
			if (other.stopDate != null)
				return false;
		} else if (!stopDate.equals(other.stopDate))
			return false;
		if (stopTime == null) {
			if (other.stopTime != null)
				return false;
		} else if (!stopTime.equals(other.stopTime))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	
	
	
 }