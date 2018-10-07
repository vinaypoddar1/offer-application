package com.heavenhr.humanresourceservice.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heavenhr.humanresourceservice.dtos.OfferDto;
import com.heavenhr.humanresourceservice.entities.Offer;
import com.heavenhr.humanresourceservice.exceptions.NoContentException;
import com.heavenhr.humanresourceservice.repositories.OfferRepository;
import com.heavenhr.humanresourceservice.services.OfferService;
import com.heavenhr.utils.constants.Constants;

@Service
public class DefaultOfferService implements OfferService {

	@Autowired
	private OfferRepository offerRepository;

	@Override
	public void createOffer(OfferDto offerDto) {
		Offer offer = new Offer(offerDto.getJobTitle(), offerDto.getStartDate());
		this.offerRepository.addOffer(offer);
	}

	@Override
	public OfferDto readOffer(String offerTitle) throws NoContentException {
		return this.offerRepository.getOffer(offerTitle)
				.map(offer -> new OfferDto(offer.getJobTitle(), offer.getStartDate(),
						offer.getApplications().size()))
				.orElseThrow(() -> new NoContentException(Constants.NO_OFFER_AVAILABLE_WITH_NAME + offerTitle));
	}

	@Override
	public List<OfferDto> getOffers() {
		return this.offerRepository.getOffers().stream().map(offer -> new OfferDto(offer.getJobTitle(),
				offer.getStartDate(), offer.getApplications().size()))
				.collect(Collectors.toList());
	}
	
	@Override
	public boolean isOfferExists(String offerTitle) {
		if(this.offerRepository.getOffer(offerTitle).isPresent())
			return true;
		return false;
	}

}
