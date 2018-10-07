package com.heavenhr.humanresourceservice.entities;

import com.heavenhr.utils.enums.Status;


public class Application {

	private String offer; // Composite
	private String candidateEmail; // unique
	private String resumeText;
	private Status applicationStatus;

	public Application(String offer, String candidateEmail, String resumeText, Status applicationStatus) {
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
