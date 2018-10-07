package com.heavenhr.humanresourceservice.utils;

@Deprecated
public class ApplicationKey {
	
	private String offerTitle;
	private String candidateEmail;
	
	public String getOfferTitle() {
		return offerTitle;
	}
	
	public void setOfferTitle(String offerTitle) {
		this.offerTitle = offerTitle;
	}

	public String getCandidateEmail() {
		return candidateEmail;
	}

	public void setCandidateEmail(String candidateEmail) {
		this.candidateEmail = candidateEmail;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof ApplicationKey) {
			ApplicationKey s = (ApplicationKey)obj;
            return this.offerTitle.equals(s.offerTitle) && this.candidateEmail.equals(s.candidateEmail);
        }
        return false;
	}

	@Override
	public int hashCode() {
		return (this.offerTitle + this.candidateEmail).hashCode();
	}

	
	
}
