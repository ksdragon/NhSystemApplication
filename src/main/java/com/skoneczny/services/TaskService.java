package com.skoneczny.services;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Time;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import javax.servlet.ServletContext;
import javax.swing.text.TabExpander;
import javax.validation.Valid;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.sl.usermodel.PaintStyle.SolidPaint;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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
			return taskRepository.findByUser(user,sort)
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
	public boolean createPdf(List<Task> findUserTasksYear, ServletContext context, String year) {
		Document document = new Document(PageSize.A4,15,15,45,30);		
		//final String FONT = "resources/fonts/AbhayaLibre-Regular.ttf";
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
			document.open();
			
			Font raportFont = FontFactory.getFont(BaseFont.COURIER, BaseFont.CP1257, 16, Font.BOLD,BaseColor.BLACK);
			Paragraph parRaport = new Paragraph("Raport godzin bosmańskich" ,raportFont);
			parRaport.setAlignment(Element.ALIGN_CENTER);
			parRaport.setIndentationLeft(50);
			parRaport.setIndentationRight(50);
			parRaport.setSpacingAfter(10);
			document.add(parRaport);			
			
			
			Font mainFont = FontFactory.getFont(BaseFont.COURIER, BaseFont.CP1257, 12, Font.NORMAL ,BaseColor.RED);
			Paragraph paragraph = new Paragraph("Lista zadań dla użytkownika: " 
								+ findUserTasksYear.get(0).getUser().getEmail() ,mainFont);
			paragraph.setAlignment(Element.ALIGN_LEFT);
			paragraph.setIndentationLeft(20);
			paragraph.setIndentationRight(50);
			paragraph.setSpacingAfter(10);
			document.add(paragraph);
			
			Font yearFont = FontFactory.getFont(BaseFont.COURIER, BaseFont.CP1257, 12, Font.NORMAL ,BaseColor.BLACK);
			Paragraph parYear = new Paragraph("Raport z" 
								+ ((year.equals("All")) ? " wszystkich lat." : ": " + year)
								,yearFont);
			parYear.setAlignment(Element.ALIGN_LEFT);
			parYear.setIndentationLeft(20);
			parYear.setIndentationRight(50);
			parYear.setSpacingAfter(10);
			document.add(parYear);
			
			long sumHours = findUserTasksYear.stream().map(x -> new Long(getMinutesInLong(x.getDuration()))).mapToLong(Long::longValue).sum();
			long h = Math.round(sumHours/60);
			long m = Math.round((((double)sumHours/60) - Math.round(sumHours/60))*100);
			Font sumHoursFont = FontFactory.getFont(BaseFont.COURIER, BaseFont.CP1257, 12, Font.NORMAL ,BaseColor.BLACK);
			Paragraph parSumHours = new Paragraph("Przepracowano: " + h + " godzin i " + m + " minut"
																
								,sumHoursFont);
			parSumHours.setAlignment(Element.ALIGN_LEFT);
			parSumHours.setIndentationLeft(20);
			parSumHours.setIndentationRight(50);
			parSumHours.setSpacingAfter(10);
			document.add(parSumHours);
			
			PdfPTable table = new PdfPTable(4);
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10);
			
			Font tableHeader = FontFactory.getFont(BaseFont.TIMES_ROMAN,BaseFont.CP1257,10,Font.NORMAL,BaseColor.BLACK);
			Font tableBody = FontFactory.getFont(BaseFont.TIMES_ROMAN,BaseFont.CP1257,9,Font.NORMAL,BaseColor.BLACK);
			
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
			
			String sCategoryTask = messageSource.getMessage("label.tasks.categoryTask", null, Locale.getDefault());
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
				cStartDateValue.setExtraParagraphSpace(5f);
				table.addCell(cStartDateValue);
								
				PdfPCell cDurationValue = new PdfPCell(new Paragraph(task.getDuration(), tableBody));
				cDurationValue.setBorderColor(BaseColor.BLACK);
				cDurationValue.setPaddingLeft(10);
				cDurationValue.setHorizontalAlignment(Element.ALIGN_CENTER);
				cDurationValue.setVerticalAlignment(Element.ALIGN_CENTER);				
				cDurationValue.setExtraParagraphSpace(5f);
				table.addCell(cDurationValue);
								
				PdfPCell cStopDateValue = new PdfPCell(new Paragraph(task.getStopDate(), tableBody));
				cStopDateValue.setBorderColor(BaseColor.BLACK);
				cStopDateValue.setPaddingLeft(10);
				cStopDateValue.setHorizontalAlignment(Element.ALIGN_CENTER);
				cStopDateValue.setVerticalAlignment(Element.ALIGN_CENTER);				
				cStopDateValue.setExtraParagraphSpace(5f);
				table.addCell(cStopDateValue);
								
				PdfPCell cCategoryTaskValue = new PdfPCell(new Paragraph(task.getCategoryTasks().getName(), tableBody));
				cCategoryTaskValue.setBorderColor(BaseColor.BLACK);
				cCategoryTaskValue.setPaddingLeft(10);
				cCategoryTaskValue.setHorizontalAlignment(Element.ALIGN_CENTER);
				cCategoryTaskValue.setVerticalAlignment(Element.ALIGN_CENTER);				
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
	private Long getMinutesInLong(String duration) {
		LocalTime durationTime = LocalTime.parse(duration);	
		return new Long(durationTime.get(ChronoField.MINUTE_OF_DAY));		
	}
	@Override
	public boolean createExcel(List<Task> findUserTasksYear, ServletContext context, String orElse) {
		
		try {
			String filePath = context.getRealPath("/resources/reports");
			File file = new File(filePath);
			boolean exists = new File(filePath).exists();
			if(!exists) {
				new File(filePath).mkdirs();				
				}
			FileOutputStream outputStream = new FileOutputStream(file+"/"+"userTasks"+".xls");
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet workSheet = workbook.createSheet(findUserTasksYear.get(0).getUser().getEmail());
			workSheet.setDefaultColumnWidth(30);
			
			HSSFCellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillForegroundColor(HSSFColorPredefined.BLUE.getIndex());
			headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			HSSFRow headerRow = workSheet.createRow(0);
			
			String sStartDate = messageSource.getMessage("label.tasks.startDate", null, Locale.getDefault());
			HSSFCell startDate = headerRow.createCell(0);
			startDate.setCellValue(sStartDate);
			startDate.setCellStyle(headerCellStyle);
			
			String sDuration = messageSource.getMessage("label.tasks.duration", null, Locale.getDefault());
			HSSFCell duration = headerRow.createCell(1);
			duration.setCellValue(sDuration);
			duration.setCellStyle(headerCellStyle);
			
			String sStopDate = messageSource.getMessage("label.tasks.stopDate", null, Locale.getDefault());
			HSSFCell stopDate = headerRow.createCell(2);
			stopDate.setCellValue(sStopDate);
			stopDate.setCellStyle(headerCellStyle);
			
			String sCategoryTask = messageSource.getMessage("label.tasks.categoryTask", null, Locale.getDefault());
			HSSFCell categoryTask = headerRow.createCell(3);
			categoryTask.setCellValue(sCategoryTask);
			categoryTask.setCellStyle(headerCellStyle);
			
			int i =1;
			for (Task task : findUserTasksYear) {	
				HSSFRow bodyRow = workSheet.createRow(i);
				
				HSSFCellStyle bodyCellStyle = workbook.createCellStyle();
				bodyCellStyle.setFillForegroundColor(HSSFColorPredefined.WHITE.getIndex());				
				
				HSSFCell startDateValue = bodyRow.createCell(0);
				startDateValue.setCellValue(task.getStartDate());
				startDateValue.setCellStyle(bodyCellStyle);
				
				HSSFCell durationValue = bodyRow.createCell(1);				
				durationValue.setCellValue(task.getDuration());
				durationValue.setCellStyle(bodyCellStyle);
				
				HSSFCell stopDateValue = bodyRow.createCell(2);
				stopDateValue.setCellValue(task.getStopDate());
				stopDateValue.setCellStyle(bodyCellStyle);
				
				HSSFCell categoryTaskValue = bodyRow.createCell(3);
				categoryTaskValue.setCellValue(task.getCategoryTasks().getName());
				categoryTaskValue.setCellStyle(bodyCellStyle);
				i++;				
			}
			
			workbook.write(outputStream);
			outputStream.flush();
			outputStream.close();
			return true;
			
		}catch (Exception e) {
				return false;
			}	
	}
}
