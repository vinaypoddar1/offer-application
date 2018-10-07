package com.heavenhr.humanresourceservice.errors;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

public class RestError implements Serializable {
	
	private static final long serialVersionUID = 8822544992036576087L;

	private String status;
	private Integer code;
	private String message;

	public RestError() {
	}
	
	public RestError(HttpStatus status, String message) {
		this.status = status.getReasonPhrase();
		this.setCode(status.value());
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
	
	

}
