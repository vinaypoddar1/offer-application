package com.heavenhr.humanresourceservice.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heavenhr.humanresourceservice.dtos.ApplicationDto;
import com.heavenhr.humanresourceservice.dtos.CandidateDto;
import com.heavenhr.humanresourceservice.services.ApplicationService;

@RestController
@RequestMapping(value = "heavenHR/v1/applications", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ApplicationController {

	@Autowired
	private ApplicationService applicationService;

	@PostMapping("/email")
	public ResponseEntity<List<ApplicationDto>> getApplicationsByEmail(@Valid @RequestBody CandidateDto candidate) {
		return ResponseEntity.ok().body(this.applicationService.getApplicatonsByEmail(candidate.getEmail()));
	}

}
