package com.heavenhr.candidateservice.dtos;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.heavenhr.utils.constants.Constants;
import com.heavenhr.utils.enums.Status;

public class ApplicationDto implements Serializable {

	private static final long serialVersionUID = -191815400508365317L;
	
	private String offer;
	private String candidateEmail;
	private String resumeText;
	private Status applicationStatus = Status.APPLIED;
	
	public ApplicationDto() {
		super();
	}

	public ApplicationDto(String offer, String candidateEmail, String resumeText) {
		this.offer = offer;
		this.candidateEmail = candidateEmail;
		this.resumeText = resumeText;
	}
	
	public ApplicationDto(String offer, String candidateEmail, String resumeText, Status applicationStatus) {
		this.offer = offer;
		this.candidateEmail = candidateEmail;
		this.resumeText = resumeText;
		this.applicationStatus = applicationStatus;
	}

	public String getOffer() {
		return offer;
	}

	public void setOffer(String offer) {
		this.offer = offer;
	}

	public String getCandidateEmail() {
		return candidateEmail;
	}

	public void setCandidateEmail(String candidateEmail) {
		this.candidateEmail = candidateEmail;
	}

	public String getResumeText() {
		return resumeText;
	}

	public void setResumeText(String resumeText) {
		this.resumeText = resumeText;
	}

	public Status getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(Status applicationStatus) {
		this.applicationStatus = applicationStatus;
	}
}
