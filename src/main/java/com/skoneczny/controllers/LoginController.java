package com.skoneczny.controllers;

import java.util.Locale;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.skoneczny.CustomAuthenticationFailureHandler;
import com.skoneczny.annotation.PasswordConstraintValidator;
import com.skoneczny.api.IMailService;
import com.skoneczny.api.IUserService;
import com.skoneczny.api.IVerificationToken;
import com.skoneczny.entites.User;
import com.skoneczny.event.OnRegistrationCompleteEvent;

@Controller
public class LoginController {

	private final Logger logger = Logger.getLogger(LoginController.class);

	@Autowired
	ApplicationEventPublisher eventPublisher;
	@Autowired
	private IUserService userService;
	@Autowired
	private IVerificationToken iVerificationToken;
	@Autowired
	IMailService iMailService;
	@Autowired
	private MessageSource messageSource;

	@GetMapping("/resetToken")
	public String resetToken(@RequestParam String email, WebRequest request, RedirectAttributes redirectAttributes) {

		logger.info("Send activate link for : " + email);

		if (userService.isUserPresent(email)) {
			User getRegistered = userService.findOne(email);
			iVerificationToken.deleteVerificationTokenByEmail(email);
			try {
				String appUrl = request.getContextPath();
				eventPublisher
						.publishEvent(new OnRegistrationCompleteEvent(getRegistered, request.getLocale(), appUrl));
			} catch (Exception me) {
				logger.info("Exception occured after send activate link on : " + email);
				return "redirect:/login";
			}
			redirectAttributes.addFlashAttribute("message", "Success");
			return "redirect:/login";

		} else {
			redirectAttributes.addFlashAttribute("message", "UnSuccess");
			return "views/login";
		}
	}

	@GetMapping("/login")
	public String showLoginPage() {
		return "views/loginForm";
	}

//		@PostMapping("/login")
//		public String loginPage(@RequestParam String email) {
//			logger.info("Post login page for : " + email);
//			return "views/loginForm";
//		}

	@GetMapping("/resetPassword")
	public String resetPassword(@RequestParam(defaultValue = "") String email) {
		logger.info("Reset Password page for: " + email);
		return "views/resetPasswordForm";
	}

	@PostMapping("/resetPassword")
	public String postResetPassword(@RequestParam(defaultValue = "") String email, WebRequest request,
			RedirectAttributes redirectAttributes) {
		logger.info("postResetPassword for: " + email);

		if (iMailService.defaultMailServerIsAvailable()) {
			logger.info("Mail server is available");
			if (userService.isUserPresent(email)) {
				PasswordConstraintValidator passwordGenerate = new PasswordConstraintValidator();
				String generateRanndomPassword = passwordGenerate.generateRanndomPassword();
//				boolean valid = passwordGenerate.isValid(generateRanndomPassword, null);				
				User user = userService.findOne(email);
				userService.setNewPassword(email, generateRanndomPassword);				
				SimpleMailMessage message = mailMessageTemplate(user, generateRanndomPassword);
				iMailService.send(message);
				redirectAttributes.addFlashAttribute("messageMessageSent", true);
			}else
			{
				SimpleMailMessage message = iMailService.isNotAvailableMessageForEmail(email);
				iMailService.send(message);				
			}
		} else {
			redirectAttributes.addFlashAttribute("messageMailServerNotAvailable", true);
			return "redirect:/resetPassword";
		}

		redirectAttributes.addFlashAttribute("messageMessageSent", true);
		return "redirect:/resetPassword";
	}

	private SimpleMailMessage mailMessageTemplate(User user, String password) {

		String recipientAddress = user.getEmail();
		String subject = messageSource.getMessage("message.login.MailSubject", null, Locale.getDefault());
		String message = messageSource.getMessage("message.login.MailBody", new Object[] { user.getName(), password },
				Locale.getDefault());
		message += messageSource.getMessage("message.contact.mailRequest", new Object[] { "admin@admin.pl" },
				Locale.getDefault());
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(message);
		return email;
	}

}
