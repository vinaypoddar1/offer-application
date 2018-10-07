package com.heavenhr.candidateservice.feign;

import java.util.List;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.heavenhr.candidateservice.dtos.ApplicationDto;
import com.heavenhr.candidateservice.dtos.CandidateDto;

@FeignClient(name="human-resource-service")
public interface HumanResourceServiceProxy {
	
	@PostMapping("heavenHR/v1/applications/email")
	public ResponseEntity<List<ApplicationDto>> getApplicationsByEmail(@Valid @RequestBody CandidateDto candidate);

}
