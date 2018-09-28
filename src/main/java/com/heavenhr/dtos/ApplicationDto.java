package com.heavenhr.dtos;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.heavenhr.utils.Status;
import com.heavenhr.validations.UniqueApplication;
import com.heavenhr.validations.ValidOfferTitle;

@UniqueApplication
public class ApplicationDto implements Serializable {

	private static final long serialVersionUID = -191815400508365317L;
	
	private String offer; 			// Composite
	@NotBlank(message = "Email must mot be blank")
	@Email(message = "Email format not correct")
	private String candidateEmail; 	// unique
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

	public String getOfferTitle() {
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
