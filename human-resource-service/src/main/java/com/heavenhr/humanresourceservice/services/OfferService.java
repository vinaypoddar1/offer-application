package com.heavenhr.humanresourceservice.services;

import java.util.List;

import com.heavenhr.humanresourceservice.dtos.OfferDto;
import com.heavenhr.humanresourceservice.exceptions.NoContentException;

public interface OfferService {

	void createOffer(OfferDto offerDto);

	OfferDto readOffer(String offerTitle) throws NoContentException;

	List<OfferDto> getOffers();

	boolean isOfferExists(String offerTitle);

}
