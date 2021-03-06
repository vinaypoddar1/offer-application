package com.heavenhr.dtos;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.heavenhr.utils.Constants;
import com.heavenhr.validations.UniqueTitle;

public class OfferDto implements Serializable {

	private static final long serialVersionUID = -7408482544173954059L;

	@NotBlank(message = Constants.TITLE_MUST_NOT_BE_BLANK)
	@UniqueTitle
	private String jobTitle; // unique
	private Date startDate;
	private Integer numberOfApplications = 0;

	public OfferDto() {
		super();
	}

	public OfferDto(String jobTitle, Date startDate, Integer numberOfApplications) {
		super();
		this.jobTitle = jobTitle;
		this.startDate = startDate;
		this.numberOfApplications = numberOfApplications;
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

}
