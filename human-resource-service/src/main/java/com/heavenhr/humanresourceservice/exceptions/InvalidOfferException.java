package com.heavenhr.humanresourceservice.exceptions;

public class InvalidOfferException extends Exception {

	private static final long serialVersionUID = -5496439968695041788L;
	
	private String customMessage;

	public InvalidOfferException(String customMessage) {
		this.customMessage = customMessage;
	}

	@Override
	public String getMessage() {
		return customMessage;
	}
}
