package com.skoneczny.initiate;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skoneczny.entites.CategoryTask;
import com.skoneczny.entites.Task;
import com.skoneczny.repositories.CategoryTaskRepository;


@Service
public class GenarateTask {
	
	@Autowired
	private CategoryTaskRepository categoryTasks;	
	private static final int DATE_IN_DAYS = 30;
	private static final int TIME_IN_MINUTES = 300;
	private final LocalTime timeAllDay = LocalTime.parse("23:59:59");
	private final Random random = new Random(); 
	private final SimpleDateFormat formatDuration = new SimpleDateFormat("HH:mm");	
	
	public Task generate() {		
		Task task = new Task();
		LocalDate randomStartDate = getRandomDate();
		task.setStartDate(randomStartDate.toString());
		LocalTime randomTimeSpanInMinutes = getRandomTimeSpanInMinutes();		
		task.setDuration(randomTimeSpanInMinutes.toString());		
		Long durationInMinutes = new Long(randomTimeSpanInMinutes.getMinute());
		LocalTime randomStartTime =  getRandomTimeSpanInMinutes();
		task.setStartTime(randomStartTime.toString());		
		if(randomStartTime.plusMinutes(durationInMinutes).isAfter(timeAllDay)) {
			LocalTime stopTime = randomStartTime.plusMinutes(durationInMinutes).minusMinutes(1440l);
			task.setStopDate(randomStartDate.plusDays(1l).toString());	
			task.setStopTime(stopTime.toString());			
		}else
		{
			task.setStopDate(randomStartDate.toString());
			task.setStopTime(randomStartTime.plusMinutes(durationInMinutes).toString());
		}
		CategoryTask randomcategoryTasks = getRandomcategoryTasks(categoryTasks.findAll());
		task.setCategoryTasks(randomcategoryTasks);
		task.setDescription(randomcategoryTasks.getName() + " time duration: " + randomTimeSpanInMinutes.toString());		
		return task;
	}	

	private CategoryTask getRandomcategoryTasks(List<CategoryTask> elements) {
		return elements.get(
                random.nextInt(
                        elements.size()));		
	}
	
	 private LocalDate getRandomDate() {
	        return LocalDate
	                .now()
	                .minus(
	                        getRandomNumberOfDays());
	    }

	    private Period getRandomNumberOfDays() {
	        return Period.ofDays(
	                random.nextInt(
	                        DATE_IN_DAYS));
	    }
	 private LocalTime getRandomTimeSpanInMinutes() {
		 return LocalTime
				 .now()
				 .minus(getRandomNumberOfMinutes());
	 }

	private TemporalAmount getRandomNumberOfMinutes() {
		return Duration.ofMinutes(
                random.nextInt(
                        TIME_IN_MINUTES));
	}
}
