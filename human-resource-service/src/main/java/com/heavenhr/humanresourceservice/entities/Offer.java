package com.heavenhr.humanresourceservice.entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Offer {

	private String jobTitle; // unique
	private Date startDate;
	private Integer numberOfApplications;
	private Map<String, Application> applications;

	public Offer(String jobTitle, Date startDate) {
		this.jobTitle = jobTitle;
		this.startDate = startDate;
		this.applications = new HashMap<>();
	}

	public Offer(String jobTitle, Date startDate, Map<String, Application> applications) {
		this.jobTitle = jobTitle;
		this.startDate = startDate;
		this.applications = applications;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Integer getNumberOfApplications() {
		return numberOfApplications;
	}

	public void setNumberOfApplications(Integer numberOfApplications) {
		this.numberOfApplications = numberOfApplications;
	}

	public Map<String, Application> getApplications() {
		return applications;
	}

	public void setApplications(Map<String, Application> applications) {
		this.applications = applications;
	}

}
