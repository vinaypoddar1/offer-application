package com.heavenhr.humanresourceservice.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import com.heavenhr.humanresourceservice.dtos.ApplicationDto;
import com.heavenhr.humanresourceservice.entities.Application;
import com.heavenhr.humanresourceservice.entities.Offer;
import com.heavenhr.humanresourceservice.events.StatusUpdateEvent;
import com.heavenhr.humanresourceservice.exceptions.InvalidApplicantInfoException;
import com.heavenhr.humanresourceservice.exceptions.InvalidOfferException;
import com.heavenhr.humanresourceservice.exceptions.NoContentException;
import com.heavenhr.humanresourceservice.repositories.OfferRepository;
import com.heavenhr.humanresourceservice.services.ApplicationService;
import com.heavenhr.utils.constants.Constants;
import com.heavenhr.utils.enums.Status;

@Service
public class DefaultApplicationService implements ApplicationService, ApplicationEventPublisherAware {

	@Autowired
	private OfferRepository offerRepository;
	
	private ApplicationEventPublisher publisher;
	
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}

	@Override
	public void createApplication(ApplicationDto applicationDto) throws InvalidOfferException {
		Optional<Offer> offerOptional = this.offerRepository.getOffer(applicationDto.getOffer());
		if (offerOptional.isPresent()) {
			Application application = new Application(applicationDto.getOffer(),
					applicationDto.getCandidateEmail(), applicationDto.getResumeText(),
					applicationDto.getApplicationStatus());
			offerOptional.get().getApplications().put(application.getCandidateEmail(), application);
		} else {
			throw new InvalidOfferException(Constants.INVALID_OFFER_TITLE + applicationDto.getOffer());
		}

	}

	@Override
	public ApplicationDto getApplication(String offerTitle, String email) throws InvalidOfferException, NoContentException {
		Application application;
		Optional<Offer> offerOptional = this.offerRepository.getOffer(offerTitle);
		if(offerOptional.isPresent()) {
			application = offerOptional.map(offer -> offer.getApplications().get(email))
					.orElseThrow(() -> new NoContentException(Constants.NO_DATA_FOUND_FOR_EMAIL + email));
		} else {
			throw new InvalidOfferException(Constants.INVALID_OFFER_TITLE + offerTitle);
		}
		return new ApplicationDto(application.getOffer(), application.getCandidateEmail(), application.getResumeText(),
				application.getApplicationStatus());
	}

	@Override
	public List<ApplicationDto> getApplicationsByOffer(String offerTitle) throws InvalidOfferException {
		return this.offerRepository.getOffer(offerTitle)
				.map(offer -> offer.getApplications().values().stream()
						.map(app -> new ApplicationDto(app.getOffer(), app.getCandidateEmail(), app.getResumeText(),
								app.getApplicationStatus()))
						.collect(Collectors.toList()))
				.orElseThrow(() -> new InvalidOfferException(Constants.INVALID_OFFER_TITLE + offerTitle));
	}

	@Override
	public void updateApplicationStatus(String offerTitle, String email, Status status)
			throws InvalidOfferException, InvalidApplicantInfoException {
		Optional<Offer> offerOptional = this.offerRepository.getOffer(offerTitle);
		if (offerOptional.isPresent()) {
			Optional<Application> applicationOptional = Optional
					.ofNullable(offerOptional.get().getApplications().get(email));
			if (applicationOptional.isPresent()) {
				applicationOptional.get().setApplicationStatus(status);
				this.publisher.publishEvent(new StatusUpdateEvent(this, offerOptional.get().getJobTitle(), applicationOptional.get().getCandidateEmail(), status));
			} else {
				throw new InvalidApplicantInfoException(Constants.INVALID_EMAIL_FOR_APPLICANT + email);
			}
		} else {
			throw new InvalidOfferException(Constants.INVALID_OFFER_TITLE + offerTitle);
		}
	}

	@Override
	public List<ApplicationDto> getApplicatonsByEmail(String email) {
		List<Application> applications = this.offerRepository.getOffers().stream()
				.map(offer -> offer.getApplications().get(email)).filter(app -> app != null)
				.collect(Collectors.toList());

		return applications.stream().map(app -> new ApplicationDto(app.getOffer(), app.getCandidateEmail(),
				app.getResumeText(), app.getApplicationStatus())).collect(Collectors.toList());
	}

	@Override
	public boolean isApplicationsExists(String offerTitle, String email) {
		Optional<Offer> offerOptional = this.offerRepository.getOffer(offerTitle);
		if (offerOptional.isPresent()) {
			if (Optional.ofNullable(offerOptional.get().getApplications().get(email)).isPresent()) {
				return true;
			}
		}
		return false;
	}

}
