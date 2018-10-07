package com.heavenhr.candidateservice.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.heavenhr.utils.constants.Constants;

public class CandidateDto {
	
	public CandidateDto() {
		super();
	}
	
	public CandidateDto(String email) {
		this.email = email;
	}
	
	@NotBlank(message = Constants.EMAIL_MUST_MOT_BE_BLANK)
	@Email(message = Constants.EMAIL_FORMAT_NOT_CORRECT)
	private String email;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
