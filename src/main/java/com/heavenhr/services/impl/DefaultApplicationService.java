package com.heavenhr.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import com.heavenhr.dtos.ApplicationDto;
import com.heavenhr.entities.Application;
import com.heavenhr.entities.Offer;
import com.heavenhr.events.StatusUpdateEvent;
import com.heavenhr.exceptions.InvalidApplicantInfoException;
import com.heavenhr.exceptions.InvalidOfferException;
import com.heavenhr.exceptions.NoContentException;
import com.heavenhr.repositories.OfferRepository;
import com.heavenhr.services.ApplicationService;
import com.heavenhr.utils.Constants;
import com.heavenhr.utils.Status;

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
		Optional<Offer> offerOptional = this.offerRepository.getOffer(applicationDto.getOfferTitle());
		if (offerOptional.isPresent()) {
			Application application = new Application(applicationDto.getOfferTitle(),
					applicationDto.getCandidateEmail(), applicationDto.getResumeText(),
					applicationDto.getApplicationStatus());
			offerOptional.get().getApplications().put(application.getCandidateEmail(), application);
		} else {
			throw new InvalidOfferException(Constants.INVALID_OFFER_TITLE + applicationDto.getOfferTitle());
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
