package com.skoneczny.api;

import javax.validation.Valid;

import com.skoneczny.entites.Password;
import com.skoneczny.entites.User;

public interface IPasswordService {

	Password createPassword(@Valid Password password, User user);

	boolean isCorrectPassword(String email, String oldPassword);
	
	boolean isCorrectNewPassword(String newPassword,String repeatPassword);
	
}
