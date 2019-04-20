package com.skoneczny.services;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.swing.text.TabExpander;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
	@Autowired private MessageSource messageSource;
	
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
	public List<Task> findUserTasksYear(User user, String year,Sort sort) {		
		if(!year.equals("All")) {
		return taskRepository.findByUser(user,sort)
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
  
	@Override
    public Page<?> findPaginated(Pageable pageable, List<?> listToPage) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        Sort sort = pageable.getSort();        
        List<?> list;
 
        if (listToPage.size() <= startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, listToPage.size());
            list = listToPage.subList(startItem, toIndex);
        }
 
        Page<?> taskPage
          = new PageImpl<>(list, PageRequest.of(currentPage, pageSize, sort), listToPage.size());
 
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
	@Override
	public void approvDeapprovTask(Long id) {
		Task task = taskRepository.findById(id).get();		
		if(task.getIsApproved()) {
			task.setIsApproved(false);
		}else {
			task.setIsApproved(true);
		}
		taskRepository.save(task);
	}	
	@Override
	public boolean createPdf(List<Task> findUserTasksYear, ServletContext context) {
		Document document = new Document(PageSize.A4,15,15,45,30);
		document.addLanguage(Locale.getDefault().toString());
		try {
			String filePath = context.getRealPath("/resources/reports");
			File file = new File(filePath);
			boolean exists = new File(filePath).exists();
			if(!exists) {
				new File(filePath).mkdirs();				
			}
			PdfWriter writer = PdfWriter.getInstance(
					document,
					new FileOutputStream(file+"/"+"userTasks"+".pdf"));
			writer.setLanguage(Locale.getDefault().toString());
			document.open();
			
			Font mainFont = FontFactory.getFont("Arial",BaseFont.IDENTITY_H,10,0, BaseColor.BLACK);
			Paragraph paragraph = new Paragraph("Lista zadań",mainFont);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setIndentationLeft(50);
			paragraph.setIndentationRight(50);
			paragraph.setSpacingAfter(10);
			document.add(paragraph);
			
			PdfPTable table = new PdfPTable(4);
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10);
			
			Font tableHeader = FontFactory.getFont("Arial",10,BaseColor.BLACK);
			Font tableBody = FontFactory.getFont("Arial",9,BaseColor.BLACK);
			
			float[] columnWidths = {2f,2f,2f,2f};
			table.setWidths(columnWidths);
			
			String sStartDate = messageSource.getMessage("label.tasks.startDate", null, Locale.getDefault());
			PdfPCell cStartDate = new PdfPCell(new Paragraph(sStartDate, tableHeader));
			cStartDate.setBorderColor(BaseColor.BLACK);
			cStartDate.setPaddingLeft(10);
			cStartDate.setHorizontalAlignment(Element.ALIGN_CENTER);
			cStartDate.setVerticalAlignment(Element.ALIGN_CENTER);
			cStartDate.setBackgroundColor(BaseColor.GRAY);
			cStartDate.setExtraParagraphSpace(5f);
			table.addCell(cStartDate);
			
			String sDuration = messageSource.getMessage("label.tasks.duration", null, Locale.getDefault());
			PdfPCell cDuration = new PdfPCell(new Paragraph(sDuration, tableHeader));
			cDuration.setBorderColor(BaseColor.BLACK);
			cDuration.setPaddingLeft(10);
			cDuration.setHorizontalAlignment(Element.ALIGN_CENTER);
			cDuration.setVerticalAlignment(Element.ALIGN_CENTER);
			cDuration.setBackgroundColor(BaseColor.GRAY);
			cDuration.setExtraParagraphSpace(5f);
			table.addCell(cDuration);
			
			String sStopDate = messageSource.getMessage("label.tasks.stopDate", null, Locale.getDefault());
			PdfPCell cStopDate = new PdfPCell(new Paragraph(sStopDate, tableHeader));
			cStopDate.setBorderColor(BaseColor.BLACK);
			cStopDate.setPaddingLeft(10);
			cStopDate.setHorizontalAlignment(Element.ALIGN_CENTER);
			cStopDate.setVerticalAlignment(Element.ALIGN_CENTER);
			cStopDate.setBackgroundColor(BaseColor.GRAY);
			cStopDate.setExtraParagraphSpace(5f);
			table.addCell(cStopDate);
			
			String sCategoryTask = messageSource.getMessage("label.tasks.stopDate", null, Locale.getDefault());
			PdfPCell cCategoryTask = new PdfPCell(new Paragraph(sCategoryTask, tableHeader));
			cCategoryTask.setBorderColor(BaseColor.BLACK);
			cCategoryTask.setPaddingLeft(10);
			cCategoryTask.setHorizontalAlignment(Element.ALIGN_CENTER);
			cCategoryTask.setVerticalAlignment(Element.ALIGN_CENTER);
			cCategoryTask.setBackgroundColor(BaseColor.GRAY);
			cCategoryTask.setExtraParagraphSpace(5f);
			table.addCell(cCategoryTask);
			
			for (Task task : findUserTasksYear) {				
				PdfPCell cStartDateValue = new PdfPCell(new Paragraph(task.getStartDate(), tableBody));
				cStartDateValue.setBorderColor(BaseColor.BLACK);
				cStartDateValue.setPaddingLeft(10);
				cStartDateValue.setHorizontalAlignment(Element.ALIGN_CENTER);
				cStartDateValue.setVerticalAlignment(Element.ALIGN_CENTER);
				cStartDateValue.setBackgroundColor(BaseColor.GRAY);
				cStartDateValue.setExtraParagraphSpace(5f);
				table.addCell(cStartDateValue);
								
				PdfPCell cDurationValue = new PdfPCell(new Paragraph(task.getDuration(), tableBody));
				cDurationValue.setBorderColor(BaseColor.BLACK);
				cDurationValue.setPaddingLeft(10);
				cDurationValue.setHorizontalAlignment(Element.ALIGN_CENTER);
				cDurationValue.setVerticalAlignment(Element.ALIGN_CENTER);
				cDurationValue.setBackgroundColor(BaseColor.GRAY);
				cDurationValue.setExtraParagraphSpace(5f);
				table.addCell(cDurationValue);
								
				PdfPCell cStopDateValue = new PdfPCell(new Paragraph(task.getStopDate(), tableBody));
				cStopDateValue.setBorderColor(BaseColor.BLACK);
				cStopDateValue.setPaddingLeft(10);
				cStopDateValue.setHorizontalAlignment(Element.ALIGN_CENTER);
				cStopDateValue.setVerticalAlignment(Element.ALIGN_CENTER);
				cStopDateValue.setBackgroundColor(BaseColor.GRAY);
				cStopDateValue.setExtraParagraphSpace(5f);
				table.addCell(cStopDateValue);
								
				PdfPCell cCategoryTaskValue = new PdfPCell(new Paragraph(task.getCategoryTasks().getName(), tableBody));
				cCategoryTaskValue.setBorderColor(BaseColor.BLACK);
				cCategoryTaskValue.setPaddingLeft(10);
				cCategoryTaskValue.setHorizontalAlignment(Element.ALIGN_CENTER);
				cCategoryTaskValue.setVerticalAlignment(Element.ALIGN_CENTER);
				cCategoryTaskValue.setBackgroundColor(BaseColor.GRAY);
				cCategoryTaskValue.setExtraParagraphSpace(5f);
				table.addCell(cCategoryTaskValue);				
			}
			
			document.add(table);
			document.close();
			writer.close();
			return true;
			
		}catch (Exception e) {
			return false;
		}				
	}
}
