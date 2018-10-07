package com.heavenhr.humanresourceservice.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.heavenhr.humanresourceservice.services.OfferService;

public class OfferTitleValidator implements ConstraintValidator<ValidOfferTitle, String>{

	@Autowired
	private OfferService offerService;
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return this.offerService.isOfferExists(value);
	}

}
