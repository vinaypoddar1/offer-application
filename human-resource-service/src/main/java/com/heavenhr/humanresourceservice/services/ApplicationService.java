package com.heavenhr.humanresourceservice.services;

import java.util.List;

import com.heavenhr.humanresourceservice.dtos.ApplicationDto;
import com.heavenhr.humanresourceservice.exceptions.InvalidApplicantInfoException;
import com.heavenhr.humanresourceservice.exceptions.InvalidOfferException;
import com.heavenhr.humanresourceservice.exceptions.NoContentException;
import com.heavenhr.utils.enums.Status;

public interface ApplicationService {

	void createApplication(ApplicationDto applicationDto) throws InvalidOfferException;

	ApplicationDto getApplication(String offerTitle, String email) throws InvalidOfferException, NoContentException;

	List<ApplicationDto> getApplicationsByOffer(String offerTitle) throws InvalidOfferException;

	void updateApplicationStatus(String offerTitle, String email, Status status)
			throws InvalidOfferException, InvalidApplicantInfoException;

	List<ApplicationDto> getApplicatonsByEmail(String email);

	boolean isApplicationsExists(String offerTitle, String email);

}
