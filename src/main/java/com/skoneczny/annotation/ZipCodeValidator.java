package com.skoneczny.annotation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ZipCodeValidator 
	implements ConstraintValidator<ValidZipCode, String> {
		   
		  private Pattern pattern;
		  private Matcher matcher;
		  private static final String ZIP_CODE_PATTERN = "^[0-9]{2}(?:-[0-9]{3})?$";
			  
		  @Override
		  public void initialize(ValidZipCode constraintAnnotation) {       
		  }
		  @Override
		  public boolean isValid(String zpiCode, ConstraintValidatorContext context){   
		      return (validateZipCode(zpiCode));
		  } 
		  private boolean validateZipCode(String zpiCode) {
		      pattern = Pattern.compile(ZIP_CODE_PATTERN);
		      matcher = pattern.matcher(zpiCode);
		      return matcher.matches();
  }
}
	

