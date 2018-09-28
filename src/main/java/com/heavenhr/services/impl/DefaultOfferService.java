package com.heavenhr.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heavenhr.dtos.OfferDto;
import com.heavenhr.entities.Offer;
import com.heavenhr.exceptions.NoContentException;
import com.heavenhr.repository.OfferRepository;
import com.heavenhr.services.OfferService;

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
				.orElseThrow(() -> new NoContentException("No Offer available with name " + offerTitle));
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
