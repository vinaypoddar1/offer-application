package com.heavenhr.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.heavenhr.dtos.ApplicationDto;
import com.heavenhr.dtos.CandidateDto;
import com.heavenhr.dtos.OfferDto;
import com.heavenhr.exceptions.InvalidApplicantInfoException;
import com.heavenhr.exceptions.InvalidOfferException;
import com.heavenhr.exceptions.NoContentException;
import com.heavenhr.services.ApplicationService;
import com.heavenhr.services.OfferService;

@RestController
@RequestMapping(value = "/heavenHR/v1/offers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class OfferController {

	@Autowired
	private OfferService offerService;

	@Autowired
	private ApplicationService applicationService;

	@ResponseStatus(value = HttpStatus.CREATED, reason = "Offer Created Successfully")
	@PostMapping
	public void createOffer(@Valid @RequestBody OfferDto offer) {
		this.offerService.createOffer(offer);
	}

	@GetMapping("/{offerTitle}")
	public ResponseEntity<OfferDto> readOffer(@PathVariable String offerTitle) throws NoContentException {
		return ResponseEntity.ok().body(this.offerService.readOffer(offerTitle));
	}

	@GetMapping
	public ResponseEntity<List<OfferDto>> getOffers() throws NoContentException {
		return ResponseEntity.ok().body(this.offerService.getOffers());
	}

	@GetMapping("/{offerTitle}/applications")
	public ResponseEntity<List<ApplicationDto>> getApplicationsByOffer(@PathVariable String offerTitle)
			throws InvalidOfferException {
		return ResponseEntity.ok().body(this.applicationService.getApplicationsByOffer(offerTitle));
	}

	@PostMapping("/{offerTitle}/applications/track")
	public ResponseEntity<ApplicationDto> getApplication(@Valid @RequestBody CandidateDto candidate,
			@PathVariable String offerTitle) throws InvalidOfferException, NoContentException {
		return ResponseEntity.ok().body(this.applicationService.getApplication(offerTitle, candidate.getEmail()));
	}

	@ResponseStatus(value = HttpStatus.CREATED, reason = "Application Created Successfully")
	@PostMapping("/{offerTitle}/applications")
	public void applyOffer(@Valid @RequestBody ApplicationDto application, @PathVariable String offerTitle)
			throws InvalidOfferException {
		this.applicationService.createApplication(application);
	}

	@ResponseStatus(value = HttpStatus.OK, reason = "Application Status Updated Successfully")
	@PutMapping("/{offerTitle}/applications/update")
	public void updateStatus(@Valid @RequestBody CandidateDto candidate, @PathVariable String offerTitle)
			throws InvalidOfferException, InvalidApplicantInfoException {
		this.applicationService.updateApplicationStatus(offerTitle, candidate.getEmail(), candidate.getStatus());
	}

}
