package com.heavenhr.services;

import java.util.List;

import com.heavenhr.dtos.OfferDto;
import com.heavenhr.exceptions.NoContentException;

public interface OfferService {

	void createOffer(OfferDto offerDto);

	OfferDto readOffer(String offerTitle) throws NoContentException;

	List<OfferDto> getOffers();

	boolean isOfferExists(String offerTitle);

}
