package com.skoneczny.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.FillPatternType;
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
import com.skoneczny.wrapper.TimeInLongValue;



/**
 * @author HP ProDesk
 *
 */
@Service
public class TaskService implements ITaskService{
	
	@Autowired TaskRepository taskRepository;
	@Autowired private MessageSource messageSource;
	@Autowired	private ServletContext context;	
	
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
	public TreeSet<String> getAllYeas(List<User> user){
		List<String> yList = new ArrayList<>();
		List<Task> tasks = taskRepository.findByUserIn(user);
		for (Task task : tasks) {
			yList.add(task.getStartDate().substring(0,4));			
		}
		TreeSet<String> yeasList = new  TreeSet<String>(yList);
		yeasList = (TreeSet<String>)yeasList.descendingSet();
		return yeasList;		
	}
	
	
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
		

/*	public List<Task> findUserTasksYear(User user, String year) {
		List<Task> tList = new ArrayList<>();
		
		List<Task> tasks = taskRepository.findByUser(user);
		for (Task task : tasks) {
			if(task.getDate().substring(0,4).equals((year))) {
				tList.add(task);
			};
		}
		return tList;
	}
*/	
	
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
			long startTimeInMinutes = new Long(startTime.get(ChronoField.MINUTE_OF_DAY));			
			long minuteDuration = new Long(durationTime.get(ChronoField.MINUTE_OF_DAY));
			if(startTimeInMinutes + minuteDuration > 1440) return false; 
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
		String sYes = messageSource.getMessage("global.settings.yes", null, Locale.getDefault());
		String sNo = messageSource.getMessage("global.settings.no", null, Locale.getDefault());
		
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
			
			
			TimeInLongValue timeinLongValue = getTimeinLongValue(findUserTasksYear);
			
			Font sumHoursFont = FontFactory.getFont(BaseFont.COURIER, BaseFont.CP1257, 12, Font.NORMAL ,BaseColor.BLACK);
			Paragraph parSumHours = new Paragraph(timeinLongValue.toStingHoursMinutes(),sumHoursFont);
			parSumHours.setAlignment(Element.ALIGN_LEFT);
			parSumHours.setIndentationLeft(20);
			parSumHours.setIndentationRight(50);
			parSumHours.setSpacingAfter(10);
			document.add(parSumHours);
			
			PdfPTable table = new PdfPTable(5);
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10);
			
			Font tableHeader = FontFactory.getFont(BaseFont.TIMES_ROMAN,BaseFont.CP1257,10,Font.NORMAL,BaseColor.BLACK);
			Font tableBody = FontFactory.getFont(BaseFont.TIMES_ROMAN,BaseFont.CP1257,9,Font.NORMAL,BaseColor.BLACK);
			
			float[] columnWidths = {2f,2f,2f,2f,2f};
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
			
			String stimeApprovedHours = messageSource.getMessage("label.tasks.timeApprovedHours", null, Locale.getDefault());
			PdfPCell ctimeApprovedHours = new PdfPCell(new Paragraph(stimeApprovedHours, tableHeader));
			ctimeApprovedHours.setBorderColor(BaseColor.BLACK);
			ctimeApprovedHours.setPaddingLeft(10);
			ctimeApprovedHours.setHorizontalAlignment(Element.ALIGN_CENTER);
			ctimeApprovedHours.setVerticalAlignment(Element.ALIGN_CENTER);
			ctimeApprovedHours.setBackgroundColor(BaseColor.GRAY);
			ctimeApprovedHours.setExtraParagraphSpace(5f);
			table.addCell(ctimeApprovedHours);
			
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
				
				PdfPCell ctimeApprovedHoursValue = new PdfPCell(new Paragraph(task.getIsApproved()? sYes : sNo, tableBody));
				ctimeApprovedHoursValue.setBorderColor(BaseColor.BLACK);
				ctimeApprovedHoursValue.setPaddingLeft(10);
				ctimeApprovedHoursValue.setHorizontalAlignment(Element.ALIGN_CENTER);
				ctimeApprovedHoursValue.setVerticalAlignment(Element.ALIGN_CENTER);				
				ctimeApprovedHoursValue.setExtraParagraphSpace(5f);
				table.addCell(ctimeApprovedHoursValue);		
				
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
			createExcelSheetsBody(findUserTasksYear, workbook,findUserTasksYear.get(0).getUser());
			
			workbook.write(outputStream);
			outputStream.flush();
			outputStream.close();
			return true;
			
		}catch (Exception e) {
				return false;
			}	
	}
	
	private void createExcelSheetsBody(List<Task> findUserTasksYear, HSSFWorkbook workbook, User user) {
		TimeInLongValue timeinLongValue = getTimeinLongValue(findUserTasksYear);
		String sYes = messageSource.getMessage("global.settings.yes", null, Locale.getDefault());
		String sNo = messageSource.getMessage("global.settings.no", null, Locale.getDefault());
		
		HSSFSheet workSheet = workbook.createSheet(user.getEmail());
		workSheet.setDefaultColumnWidth(30);
		
		HSSFCellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFillForegroundColor(HSSFColorPredefined.GREY_40_PERCENT.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		HSSFRow headerRow = workSheet.createRow(0);
		
		String sno = messageSource.getMessage("global.settings.numberOrdinal", null, Locale.getDefault());
		HSSFCell nO = headerRow.createCell(0);
		nO.setCellValue(sno);
		nO.setCellStyle(headerCellStyle);
		
		String sStartDate = messageSource.getMessage("label.tasks.startDate", null, Locale.getDefault());
		HSSFCell startDate = headerRow.createCell(1);
		startDate.setCellValue(sStartDate);
		startDate.setCellStyle(headerCellStyle);
		
		String sDuration = messageSource.getMessage("label.tasks.duration", null, Locale.getDefault());
		HSSFCell duration = headerRow.createCell(2);
		duration.setCellValue(sDuration);
		duration.setCellStyle(headerCellStyle);
		
		String sStopDate = messageSource.getMessage("label.tasks.stopDate", null, Locale.getDefault());
		HSSFCell stopDate = headerRow.createCell(3);
		stopDate.setCellValue(sStopDate);
		stopDate.setCellStyle(headerCellStyle);
		
		String sCategoryTask = messageSource.getMessage("label.tasks.categoryTask", null, Locale.getDefault());
		HSSFCell categoryTask = headerRow.createCell(4);
		categoryTask.setCellValue(sCategoryTask);
		categoryTask.setCellStyle(headerCellStyle);
		
		String sHours = messageSource.getMessage("global.settings.hours", null, Locale.getDefault());
		HSSFCell hours = headerRow.createCell(5);
		hours.setCellValue(sHours);
		hours.setCellStyle(headerCellStyle);
		
		String sMinutes = messageSource.getMessage("global.settings.minutes", null, Locale.getDefault());
		HSSFCell minutes = headerRow.createCell(6);
		minutes.setCellValue(sMinutes);
		minutes.setCellStyle(headerCellStyle);
		
		String sIsApproved = messageSource.getMessage("label.tasks.timeApprovedHours", null, Locale.getDefault());
		HSSFCell isApproved = headerRow.createCell(7);
		isApproved.setCellValue(sIsApproved);
		isApproved.setCellStyle(headerCellStyle);
		
		HSSFCellStyle bodyCellStyle = workbook.createCellStyle();
		bodyCellStyle.setFillForegroundColor(HSSFColorPredefined.WHITE.getIndex());
		
		int i =1;
		for (Task task : findUserTasksYear) {
			List<Task> listTaskUser = new ArrayList<Task>();
			listTaskUser.add(task);
			TimeInLongValue timeinLongPerUserValue = getTimeinLongValue(listTaskUser);
			
			HSSFRow bodyRow = workSheet.createRow(i);
			
			HSSFCell nOValue = bodyRow.createCell(0);
			nOValue.setCellValue(i);
			nOValue.setCellStyle(bodyCellStyle);
			
			HSSFCell startDateValue = bodyRow.createCell(1);
			startDateValue.setCellValue(task.getStartDate());
			startDateValue.setCellStyle(bodyCellStyle);
			
			HSSFCell durationValue = bodyRow.createCell(2);				
			durationValue.setCellValue(task.getDuration());
			durationValue.setCellStyle(bodyCellStyle);
			
			HSSFCell stopDateValue = bodyRow.createCell(3);
			stopDateValue.setCellValue(task.getStopDate());
			stopDateValue.setCellStyle(bodyCellStyle);
			
			HSSFCell categoryTaskValue = bodyRow.createCell(4);
			categoryTaskValue.setCellValue(task.getCategoryTasks().getName());
			categoryTaskValue.setCellStyle(bodyCellStyle);
			
			HSSFCell hoursValue = bodyRow.createCell(5);
			hoursValue.setCellValue(timeinLongPerUserValue.getH());
			hoursValue.setCellStyle(bodyCellStyle);
			
			HSSFCell minutesValue = bodyRow.createCell(6);
			minutesValue.setCellValue(timeinLongPerUserValue.getM());
			minutesValue.setCellStyle(bodyCellStyle);
			
			HSSFCell isApprovedValue = bodyRow.createCell(7);
			isApprovedValue.setCellValue(task.getIsApproved()? sYes : sNo);
			isApprovedValue.setCellStyle(bodyCellStyle);
			
			i++;				
		}		
		HSSFRow sumRow = workSheet.createRow(i);
		String sSum = messageSource.getMessage("label.tasks.sum", null, Locale.getDefault());
		HSSFCell sum = sumRow.createCell(0);
		sum.setCellValue(sSum);
		sum.setCellStyle(headerCellStyle);
		
		String sAllHourMinutes = messageSource.getMessage("timeInLongValue.toString",
				new Object[] { timeinLongValue.getH(), timeinLongValue.getM() },
				Locale.getDefault());
		HSSFCell allHourMinutes = sumRow.createCell(1);
		allHourMinutes.setCellValue(sAllHourMinutes);
		allHourMinutes.setCellStyle(bodyCellStyle);
		
		for (int x=0; x<8; x++) {
			workSheet.autoSizeColumn(x);
		}
	}
	@Override
	public boolean createExcelAllUsersTasks(List<User> usersList, String selectedYear, Sort sortP, ServletContext context) {
		
		try {
			String filePath = context.getRealPath("/resources/reports");
			File file = new File(filePath);
			boolean exists = new File(filePath).exists();
			if(!exists) {
				new File(filePath).mkdirs();				
				}
			FileOutputStream outputStream = new FileOutputStream(file+"/"+"userAllTasks"+".xls");
			HSSFWorkbook workbook = new HSSFWorkbook();	
			String sHeaderRaport = messageSource.getMessage("label.tasks.raportAllUsers", null, Locale.getDefault());			
			HSSFSheet workSheet = workbook.createSheet(sHeaderRaport);			
			
			int i =1;
			for (User user: usersList ) {
				
				//dopisać jak będzie all
				if (!isEmptyForSelectedYear(selectedYear, user)) {
					List<Task> userTasks = findUserTasksYear(user,selectedYear,sortP);
					if(!userTasks.isEmpty())createExcelSheetRaportAllUsers(workbook, userTasks, workSheet,i);
				}else {
					List<Task> userTasks = new ArrayList<Task>();					
					userTasks.add(new Task());
					userTasks.get(0).setUser(user);
					if(!userTasks.isEmpty())createExcelSheetRaportAllUsers(workbook, userTasks, workSheet,i);
				}
				i++;
				
				
//				List<Task> userTasks = findUserTasksYear(user,selectedYear,sortP);
//				createExcelSheetRaportAllUsers(workbook, userTasks, workSheet,i);
//				i++;
			}
			for (User user: usersList ) {
				
				List<Task> userTasks = findUserTasksYear(user,selectedYear,sortP);
				createExcelSheetsBody(userTasks, workbook,user);
			}
			
			
			workbook.write(outputStream);
			outputStream.flush();
			outputStream.close();
			return true;
			
		}catch (Exception e) {
				e.printStackTrace();
				return false;
			}
	}
	private boolean isEmptyForSelectedYear(String selectedYear, User user) {
		if(selectedYear.equals("All")) {return false;}
		return taskRepository.findByUser(user)
		.stream()
		.filter(task -> Objects.equals(task.getStartDate().substring(0,4), selectedYear))
		.collect(Collectors.toList()).isEmpty();			
	}
	private void createExcelSheetRaportAllUsers(HSSFWorkbook workbook, List<Task> userTasks, HSSFSheet workSheet, Integer i) {
		TimeInLongValue timeinLongValue = getTimeinLongValue(userTasks);
		
		HSSFCellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFillForegroundColor(HSSFColorPredefined.GREY_40_PERCENT.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		HSSFCellStyle bodyCellStyle = workbook.createCellStyle();
		bodyCellStyle.setFillForegroundColor(HSSFColorPredefined.WHITE.getIndex());
		
		//Heder
		HSSFRow headerRow = workSheet.createRow(0);		
		
		String sNo = messageSource.getMessage("global.settings.numberOrdinal", null, Locale.getDefault());
		HSSFCell nO = headerRow.createCell(0);
		nO.setCellValue(sNo);
		nO.setCellStyle(headerCellStyle);
		
		String sUser = messageSource.getMessage("label.tasks.user", null, Locale.getDefault());
		HSSFCell user = headerRow.createCell(1);
		user.setCellValue(sUser);
		user.setCellStyle(headerCellStyle);
		
		String sTimeApprovedHours = messageSource.getMessage("label.tasks.timeApprovedHours", null, Locale.getDefault());
		HSSFCell timeApprovedHours = headerRow.createCell(2);
		timeApprovedHours.setCellValue(sTimeApprovedHours);
		timeApprovedHours.setCellStyle(headerCellStyle);
		
		String sHours = messageSource.getMessage("global.settings.hours", null, Locale.getDefault());
		HSSFCell hours = headerRow.createCell(3);
		hours.setCellValue(sHours);
		hours.setCellStyle(headerCellStyle);
		
		String sMinutes = messageSource.getMessage("global.settings.minutes", null, Locale.getDefault());
		HSSFCell minutes = headerRow.createCell(4);
		minutes.setCellValue(sMinutes);
		minutes.setCellStyle(headerCellStyle);
		
		
		//Data		
		HSSFRow bodyRow = workSheet.createRow(i);
		workSheet.autoSizeColumn(i);
		
		HSSFCell nOValue = bodyRow.createCell(0);
		nOValue.setCellValue(i);
		nOValue.setCellStyle(bodyCellStyle);
		
		HSSFCell userValue = bodyRow.createCell(1);
		userValue.setCellValue(userTasks.get(0).getUser().getEmail());
		userValue.setCellStyle(bodyCellStyle);		
		
		String sAllHourMinutes = messageSource.getMessage("timeInLongValue.toString",
				new Object[] { timeinLongValue.getH(), timeinLongValue.getM() },
				Locale.getDefault());
		HSSFCell timeApprovedHoursValue = bodyRow.createCell(2);				
		timeApprovedHoursValue.setCellValue(sAllHourMinutes);
		timeApprovedHoursValue.setCellStyle(bodyCellStyle);
		
		HSSFCell hoursValue = bodyRow.createCell(3);
		hoursValue.setCellValue(timeinLongValue.getH());
		hoursValue.setCellStyle(bodyCellStyle);
		
		HSSFCell minutesValue = bodyRow.createCell(4);
		minutesValue.setCellValue(timeinLongValue.getM());
		minutesValue.setCellStyle(bodyCellStyle);
		
		for (int x=0; x<5; x++) {
			workSheet.autoSizeColumn(x);
		}
	}
	
	public TimeInLongValue getTimeinLongValue(List<Task> listTasks) {		
		long sumHours = listTasks.stream().filter(x -> x.getIsApproved()).map(x -> new Long(getMinutesInLong(x.getDuration()))).mapToLong(Long::longValue).sum();
		long h = Math.round(sumHours/60);		
		long m = Math.round(sumHours%60);		
		return new TimeInLongValue(h, m);		
	}
	@Override
	public void filedownload(String fullPath, HttpServletResponse response, String fileName) {
		File file = new File(fullPath);
		final int BUFFER_SIZE = 4096;
		if(file.exists()) {
			try {
				FileInputStream inputStream = new FileInputStream(file);
				String mimeType = context.getMimeType(fullPath);
				response.setContentType(mimeType);
				response.setHeader("content-disposition", "attachment; filename="+fileName);
				OutputStream outputStream = response.getOutputStream();
				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead = -1;
				while((bytesRead = inputStream.read(buffer))!= -1) {
					outputStream.write(buffer,0,bytesRead);
				}
				inputStream.close();
				outputStream.close();
				file.delete();
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	@Override
	public Page<Task> findUsersTasksPageableByYear(User user, String year, Pageable pageable) {
		if(!year.equals("All")) {
							 	
			 	List<Task> tasksList = taskRepository.findByUser(user)
				.stream()
				.filter(task -> Objects.equals(task.getStartDate().substring(0,4), year))
				.collect(Collectors.toList());
				final long count = tasksList.size();			 
				
			return new PageImpl<Task>(tasksList, pageable, count);
//				return taskRepository.findByUser(user,pageable);
			}else {
				return taskRepository.findByUser(user,pageable);
			}
	}
}
