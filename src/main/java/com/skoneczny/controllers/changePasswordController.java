package com.skoneczny.controllers;

import java.security.Principal;
import java.util.Locale;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.hibernate.query.criteria.internal.predicate.IsEmptyPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.skoneczny.api.IPasswordService;
import com.skoneczny.api.IUserService;
import com.skoneczny.entites.Password;
import com.skoneczny.entites.User;

@Controller
public class changePasswordController {
	
	private final Logger logger = Logger.getLogger(changePasswordController.class);
		
	private IUserService userService;	
	private IPasswordService iPasswordService;
	private MessageSource messageSource;
	
	@Autowired	
	public changePasswordController(IUserService userService, IPasswordService iPasswordService,
			MessageSource messageSource) {
			this.userService = userService;
			this.iPasswordService = iPasswordService;
			this.messageSource = messageSource;
	}

	@GetMapping("/changePassword")
	public String changePassword(Model model,Principal principal) {
		String email = principal.getName();		
		model.addAttribute("password", new Password());
		return "views/changePasswordForm";
	}
	
	//dorobić anotacje weryfikacyjną hasło tak aby jako parametr przyjmowała wyrażenie reguralne.
	//przeprowadzić walidację siły hasła.
	
	@PostMapping("/changePassword")
	public String changePassword(@Valid Password password,BindingResult result, Principal principal, RedirectAttributes redirectAttributes) {
		String email = principal.getName();
		boolean correctPassword = iPasswordService.isCorrectPassword(email,password.getOldPassword());
		boolean correctNewPassword = iPasswordService.isCorrectNewPassword(password.getNewPassword(), password.getRepeatPassword());		
		if(!correctPassword) {
			result.rejectValue(
					"oldPassword",
					"error.password",
					messageSource.getMessage("error.changePassword.oldPassword",null, Locale.getDefault()));
			return "views/changePasswordForm";
		}else if(!correctNewPassword){
			result.rejectValue(
					"repeatPassword",
					"error.password",
					messageSource.getMessage("error.changePassword.repeatPassword",null, Locale.getDefault()));
			return "views/changePasswordForm";
		}		
		if (result.hasErrors()){
			return "views/changePasswordForm";
		}		
			
			iPasswordService.createPassword(password, userService.findOne(email));
			userService.setNewPassword(email,password.getNewPassword());
			redirectAttributes.addFlashAttribute("changePasswordSuccess", "Success");
			logger.info("Change password for: " + email);
			return "redirect:/changePassword";		
		
//		logger.info("Password not changed for: " + email);
//		redirectAttributes.addFlashAttribute("changePasswordUnSuccess", "UnSuccess");
	}	

}
