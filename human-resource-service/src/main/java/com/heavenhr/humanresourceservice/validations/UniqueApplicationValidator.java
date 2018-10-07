package com.heavenhr.humanresourceservice.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.heavenhr.humanresourceservice.dtos.ApplicationDto;
import com.heavenhr.humanresourceservice.services.ApplicationService;

public class UniqueApplicationValidator implements ConstraintValidator<UniqueApplication, ApplicationDto> {

	@Autowired
	private ApplicationService applicationService;
	
	@Override
	public boolean isValid(ApplicationDto application, ConstraintValidatorContext context) {
		return !this.applicationService.isApplicationsExists(application.getOffer(), application.getCandidateEmail());
	}


}
