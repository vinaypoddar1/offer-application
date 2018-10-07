package com.heavenhr.humanresourceservice.exceptions;

public class InvalidApplicantInfoException extends Exception {

	private static final long serialVersionUID = -3412162947168138818L;

	private String customMessage;

	public InvalidApplicantInfoException(String customMessage) {
		this.customMessage = customMessage;
	}

	@Override
	public String getMessage() {
		return customMessage;
	}
}
