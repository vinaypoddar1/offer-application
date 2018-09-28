package com.heavenhr.validations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RUNTIME)
@Target({FIELD, ANNOTATION_TYPE,PARAMETER})
@Constraint(validatedBy = {UniqueTitleValidator.class})
public @interface UniqueTitle {
	
	String message() default "Duplicate Offer Title";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};

}
