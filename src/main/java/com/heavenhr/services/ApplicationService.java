package com.heavenhr.services;

import java.util.List;

import javax.validation.Valid;

import com.heavenhr.dtos.ApplicationDto;
import com.heavenhr.exceptions.InvalidApplicantInfoException;
import com.heavenhr.exceptions.InvalidOfferException;
import com.heavenhr.exceptions.NoContentException;
import com.heavenhr.utils.Status;

public interface ApplicationService {
	
	void createApplication(ApplicationDto applicationDto) throws InvalidOfferException;
	
	ApplicationDto getApplication(String offerTitle, String email) throws InvalidOfferException, NoContentException;
	
	List<ApplicationDto> getApplicationsByOffer(String offerTitle) throws InvalidOfferException;
	
	void updateApplicationStatus(String offerTitle, String email, Status status) throws InvalidOfferException, InvalidApplicantInfoException;
	
	List<ApplicationDto> getApplicatonsByEmail(String email);
	
	boolean isApplicationsExists(String offerTitle, String email);

}
