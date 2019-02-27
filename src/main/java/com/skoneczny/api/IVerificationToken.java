package com.skoneczny.api;

import com.skoneczny.entites.VerificationToken;

public interface IVerificationToken {
	
	VerificationToken gettVerificationTokenByEmail(String email);
	
	boolean isExpiryDate(VerificationToken verificationToken);
	
	void deleteVerificationTokenByEmail(String email);
}
