package com.heavenhr.candidateservice.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.heavenhr.candidateservice.dtos.ApplicationDto;
import com.heavenhr.candidateservice.dtos.CandidateDto;
import com.heavenhr.candidateservice.dtos.OfferDto;
import com.heavenhr.candidateservice.feign.HumanResourceServiceProxy;
import com.heavenhr.candidateservice.services.ApplicationService;

@Service
public class DefaultApplicationService implements ApplicationService {

	@Autowired
	private HumanResourceServiceProxy humanResourceServiceProxy;

	@Override
	public List<ApplicationDto> getApplicatonsByEmail(String email) throws Exception {
		
		ResponseEntity<List<ApplicationDto>> response = this.humanResourceServiceProxy.getApplicationsByEmail(new CandidateDto(email));
		return response.getBody();
	}

}
