package com.skoneczny.services;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.skoneczny.api.ITaskService;
import com.skoneczny.entites.Task;
import com.skoneczny.entites.User;
import com.skoneczny.repositories.TaskRepository;



/**
 * @author HP ProDesk
 *
 */
@Service
public class TaskService implements ITaskService{
	
	@Autowired TaskRepository taskRepository;
	
	@Override
	public void addTask(Task task, User user) {
		task.setUser(user);
		taskRepository.save(task);
	}
	@Override
	public List<Task> findUserTask(User user){
		return taskRepository.findByUser(user);
	}
	@Override
	public void deleteTask(Long id) {
		taskRepository.deleteById(id);		
	}
	
	/**
	 * Metoda używana do pobrania lat z zadań poszczególnych użytkowników
	 *@param user
	 *@return lista lat posortowana malejąco
	 */
	@Override
	public TreeSet<String> getAllYeas(User user){
		List<String> yList = new ArrayList<>();
		List<Task> tasks = taskRepository.findByUser(user);
		for (Task task : tasks) {
			yList.add(task.getStartDate().substring(0,4));			
		}
		TreeSet<String> yeasList = new  TreeSet<String>(yList);
		yeasList = (TreeSet<String>)yeasList.descendingSet();
		return yeasList;		
	}
		

	/*public List<Task> findUserTasksYear(User user, String year) {
		List<Task> tList = new ArrayList<>();
		
		List<Task> tasks = taskRepository.findByUser(user);
		for (Task task : tasks) {
			if(task.getDate().substring(0,4).equals((year))) {
				tList.add(task);
			};
		}
		return tList;
	}*/
	
	@Override
	public List<Task> findUserTasksYear(User user, String year) {		
		if(!year.equals("All")) {
		return taskRepository.findByUser(user)
				.stream()
				.filter(task -> Objects.equals(task.getStartDate().substring(0,4), year))
				.collect(Collectors.toList());
		}else {
			return taskRepository.findByUser(user)
					.stream()					
					.collect(Collectors.toList());
		}
	}
	
	@Override
	public boolean checkTimeStopIsCorrect(@Valid Task task) {
			if(task.getDuration().isEmpty()) return false;			
			LocalTime startTime = LocalTime.parse(task.getStartTime());
			LocalTime durationTime = LocalTime.parse(task.getDuration());
			long startTimeInMinuts = new Long(startTime.get(ChronoField.MINUTE_OF_DAY));			
			long minuteDuration = new Long(durationTime.get(ChronoField.MINUTE_OF_DAY));
			if(startTimeInMinuts + minuteDuration > 1440) return false; 
			return true;
	}
		
    public Page<?> findPaginated(Pageable pageable, List<?> listToPage) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<?> list;
 
        if (listToPage.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, listToPage.size());
            list = listToPage.subList(startItem, toIndex);
        }
 
        Page<?> taskPage
          = new PageImpl<>(list, PageRequest.of(currentPage, pageSize), listToPage.size());
 
        return taskPage;
    }
	
    @Override
	public String startTimePlusDuration(@Valid Task task) {
		LocalTime startTime = LocalTime.parse(task.getStartTime());
		LocalTime durationTime = LocalTime.parse(task.getDuration());
		Long minutesOfTheDuration = new Long(durationTime.get(ChronoField.MINUTE_OF_DAY));		
		String stopTime = startTime.plusMinutes(minutesOfTheDuration).toString();
		return stopTime;
	}
	@Override
	public Task findTaskById(Long id) {
		return taskRepository.findById(id).get();
		
	}

}
