package com.heavenhr.humanresourceservice.exceptions;

public class NoContentException extends Exception {

	private static final long serialVersionUID = 308567470265929329L;
	
	private String customMessage;
	
	public NoContentException(String customMessage){
		this.customMessage = customMessage;
	}

	@Override
	public String getMessage() {
		return customMessage;
	}
	
}
