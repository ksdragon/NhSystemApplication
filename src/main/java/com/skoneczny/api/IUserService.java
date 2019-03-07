package com.skoneczny.api;


import java.util.List;

import javax.validation.Valid;

import com.skoneczny.entites.Role;
import com.skoneczny.entites.User;
import com.skoneczny.entites.VerificationToken;

public interface IUserService {

	User getUser(String verificationToken);

	VerificationToken getVerificationToken(String VerificationToken);

	void saveRegisteredUser(User user);

	void createVerificationToken(User user, String token);

	boolean isUserPresent(String email);

	User createUser(@Valid User user);

	User findOne(String email);

	void setNewPassword(String email, String newPassword);

	void setRole(List<Role> roles, User user);	

}
