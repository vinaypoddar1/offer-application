package com.heavenhr.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.heavenhr.utils.Status;

public class CandidateDto {
	
	@NotBlank(message = "Email must mot be blank")
	@Email(message = "Email format not correct")
	private String email;
	private Status status;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
