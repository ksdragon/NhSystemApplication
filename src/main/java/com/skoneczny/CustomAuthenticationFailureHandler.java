package com.skoneczny;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
	
	private final static String USERNAME_PARAMETER = "username";
	@Autowired MessageSource messageSource; 
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		 String lastUserName = request.getParameter(USERNAME_PARAMETER);
		
			if(exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
				request.getSession().setAttribute("AUTH_ERROR_MSG",
		        		messageSource.getMessage("label.loginForm.alert1", null, Locale.getDefault()));				
		        response.sendRedirect(request.getContextPath() + "/login?error");
				
			   } else if (exception.getClass().isAssignableFrom(DisabledException.class)) {
				   request.getSession().setAttribute("AUTH_ERROR_MSG",
			        		messageSource.getMessage("label.loginForm.alert2", null, Locale.getDefault()));
			        response.sendRedirect(request.getContextPath() + "/login?error");
				   
			   }		
			
	}

}
