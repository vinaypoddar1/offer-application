package com.heavenhr.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heavenhr.services.OfferService;

public class UniqueTitleValidator implements ConstraintValidator<UniqueTitle, String>{

	@Autowired
	private OfferService offerService;
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return !this.offerService.isOfferExists(value);
	}

}