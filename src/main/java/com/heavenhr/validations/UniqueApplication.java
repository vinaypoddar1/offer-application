package com.heavenhr.validations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
@Constraint(validatedBy = { UniqueApplicationValidator.class })
public @interface UniqueApplication {

	String message() default "You have already applied for the offer!";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
