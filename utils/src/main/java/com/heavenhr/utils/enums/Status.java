package com.heavenhr.utils.enums;

public enum Status {
	APPLIED(1, "Applied"),
	INVITED(2, "Invited"), 
	REJECTED(3, "Rejected"), 
	HIRED(4, "Hired");
	
	private final int value;
	private final String status;
	
	Status(int value, String status) {
		this.value = value;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public int getValue() {
		return value;
	}
	
	
}
