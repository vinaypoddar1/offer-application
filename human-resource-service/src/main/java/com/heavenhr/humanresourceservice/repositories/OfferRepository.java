package com.heavenhr.humanresourceservice.repositories;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.heavenhr.humanresourceservice.entities.Application;
import com.heavenhr.humanresourceservice.entities.Offer;
import com.heavenhr.utils.enums.Status;

@Repository
public class OfferRepository {

	private Map<String, Offer> offers;

	public OfferRepository() {
		offers = new ConcurrentHashMap<>();
		addInitialData();
	}

	public void addOffer(Offer offer) {
		offers.put(offer.getJobTitle(), offer);
	}

	public Optional<Offer> getOffer(String offerTitle) {
		return Optional.ofNullable(offers.get(offerTitle));
	}

	public Collection<Offer> getOffers() {
		return this.offers.values();
	}

	private void addInitialData() {

		Map<String, Application> map = new HashMap<>();
		map.put("molly@msn.com",
				new Application("Java-Backend", "molly@msn.com", "Sample Resume Text", Status.APPLIED));
		map.put("andrea@gmail.com",
				new Application("Java-Backend", "andrea@gmail.com", "Sample Resume Text", Status.APPLIED));
		map.put("john@yahoo.com",
				new Application("Java-Backend", "john@yahoo.com", "Sample Resume Text", Status.HIRED));
		map.put("paul@aol.com", new Application("Java-Backend", "paul@aol.com", "Sample Resume Text", Status.INVITED));

		offers.put("Java-Backend", new Offer("Java-Backend", new Date(), map));

		offers.put("Java-FullStack", new Offer("Java-FullStack", new Date()));
		offers.put("Oracle", new Offer("Oracle", new Date()));

		Map<String, Application> map1 = new HashMap<>();
		map1.put("ted@msn.com", new Application("Frontend", "ted@msn.com", "Sample Resume Text", Status.APPLIED));
		map1.put("andrea@gmail.com",
				new Application("Frontend", "andrea@gmail.com", "Sample Resume Text", Status.APPLIED));
		map1.put("michele@yahoo.com",
				new Application("Frontend", "michele@yahoo.com", "Sample Resume Text", Status.HIRED));
		map1.put("paul@aol.com", new Application("Frontend", "paul@aol.com", "Sample Resume Text", Status.INVITED));
		offers.put("Frontend", new Offer("Frontend", new Date(), map1));

		offers.put("Senior-Frontend", new Offer("Senior-Frontend", new Date()));
	}

}
