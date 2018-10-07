package com.heavenhr.humanresourceservice.validations;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.heavenhr.utils.constants.Constants;


@Documented
@Retention(RUNTIME)
@Target({PARAMETER, FIELD})
@Constraint(validatedBy = {OfferTitleValidator.class})
public @interface ValidOfferTitle {
	
	String message() default Constants.OFFER_TITLE_NOT_VALID;
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};

}
