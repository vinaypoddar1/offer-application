package com.heavenhr.humanresourceservice.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.heavenhr.utils.constants.Constants;
import com.heavenhr.utils.enums.Status;

public class CandidateDto {
	
	@NotBlank(message = Constants.EMAIL_MUST_MOT_BE_BLANK)
	@Email(message = Constants.EMAIL_FORMAT_NOT_CORRECT)
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
