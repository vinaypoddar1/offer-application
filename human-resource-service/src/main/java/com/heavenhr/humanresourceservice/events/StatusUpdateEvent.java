package com.heavenhr.humanresourceservice.events;

import org.springframework.context.ApplicationEvent;

import com.heavenhr.utils.enums.Status;

public class StatusUpdateEvent extends ApplicationEvent {

	private static final long serialVersionUID = 2327888634768948177L;

	private String jobTitle;

	private String email;

	private String status;

	public StatusUpdateEvent(Object source, String jobTitle, String email, Status status) {
		super(source);
		this.jobTitle = jobTitle;
		this.email = email;
		this.status = status.getStatus();
	}

	public StatusUpdateEvent(Object source) {
		super(source);
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public String getEmail() {
		return email;
	}

	public String getStatus() {
		return status;
	}

}
