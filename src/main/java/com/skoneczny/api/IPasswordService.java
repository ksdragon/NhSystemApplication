package com.skoneczny.api;

import javax.validation.Valid;

import com.skoneczny.entites.Password;

public interface IPasswordService {

	Password createPassword(@Valid Password password);

	boolean isCorrectPassword(String email, String oldPassword);
	
	boolean isCorrectNewPassword(String newPassword,String repeatPassword);
}
