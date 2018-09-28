package com.heavenhr.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.heavenhr.dtos.ApplicationDto;
import com.heavenhr.services.ApplicationService;

public class UniqueApplicationValidator implements ConstraintValidator<UniqueApplication, ApplicationDto> {

	@Autowired
	private ApplicationService applicationService;
	
	@Override
	public boolean isValid(ApplicationDto application, ConstraintValidatorContext context) {
		return !this.applicationService.isApplicationsExists(application.getOfferTitle(), application.getCandidateEmail());
	}


}
