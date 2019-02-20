package com.skoneczny.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE}) 
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ZipCodeValidator.class)
@Documented
public @interface ValidZipCode {   
    String message() default "Invalid zip code" ; //default "Invalid annotation"
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
}

