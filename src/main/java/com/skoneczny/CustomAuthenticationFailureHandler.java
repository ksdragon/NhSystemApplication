package com.skoneczny;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.skoneczny.api.IVerificationToken;
import com.skoneczny.entites.VerificationToken;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private final static String USERNAME_PARAMETER = "username";

	// pobieranie właściwości z plików.
	@Autowired
	MessageSource messageSource;

	@Autowired
	private IVerificationToken iVerificationToken;

	// SPRING_SECURITY_LAST_EXCEPTION.authentication.principal - the way to keep
	// username after submit, use in template.
	// https://www.intertech.com/Blog/understanding-spring-mvc-model-and-session-attributes/

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		request.getSession().removeAttribute("EMAIL");
		String email = request.getParameter(USERNAME_PARAMETER);		

		// sprawdzenie czy wyjątki należą do pozszególnych klas
		if (exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
			request.getSession().setAttribute("AUTH_ERROR_MSG",
					messageSource.getMessage("label.loginForm.alert1", null, Locale.getDefault()));
			request.getSession().setAttribute("EMAIL", email);
			response.sendRedirect(request.getContextPath() + "/login?error");

		} else if (exception.getClass().isAssignableFrom(DisabledException.class)) {
			VerificationToken gettVerificationTokenByEmail = iVerificationToken.gettVerificationTokenByEmail(email);
			boolean expiryDate = iVerificationToken.isExpiryDate(gettVerificationTokenByEmail);

			if (expiryDate) {
				request.getSession().setAttribute("AUTH_ERROR_MSG",
						messageSource.getMessage("label.loginForm.alert3", null, Locale.getDefault()));
			} else {
				request.getSession().setAttribute("AUTH_ERROR_MSG",
						messageSource.getMessage("label.loginForm.alert2", null, Locale.getDefault()));
			}
			request.getSession().setAttribute("EMAIL", email);
			response.sendRedirect(request.getContextPath() + "/login?error_token=true");

		}

	}

}
