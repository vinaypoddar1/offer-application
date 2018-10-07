package com.heavenhr.candidateservice.services;

import java.util.List;

import com.heavenhr.candidateservice.dtos.ApplicationDto;

public interface ApplicationService {

	List<ApplicationDto> getApplicatonsByEmail(String email) throws Exception;

}
