package com.heavenhr.humanresourceservice.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.heavenhr.humanresourceservice.dtos.OfferDto;
import com.heavenhr.humanresourceservice.entities.Offer;
import com.heavenhr.humanresourceservice.exceptions.NoContentException;
import com.heavenhr.humanresourceservice.repositories.OfferRepository;
import com.heavenhr.humanresourceservice.services.OfferService;
import com.heavenhr.utils.constants.Constants;

@RunWith(SpringRunner.class)
public class DefaultOfferServiceTest {
	
	@TestConfiguration
    static class DefaultOfferServiceTestContextConfiguration {
  
        @Bean
        public OfferService offerService() {
            return new DefaultOfferService();
        }
    }
	
	@Autowired
	private OfferService offerService;
	
	@MockBean
	private OfferRepository offerRepository;
	
	@Before
	public void setup() throws NoContentException {
		Offer offer = new Offer(Constants.VALID_OFFER_TITLE_JAVA_BACKEND, new Date());
		when(this.offerRepository.getOffer(Constants.VALID_OFFER_TITLE_JAVA_BACKEND))
			.thenReturn(Optional.of(offer));
		
		when(this.offerRepository.getOffers()).thenReturn(Arrays.asList(offer));
	}
	
	@Test
	public void whenTitleUnique_userShouldBeAbleToCreateOffer() {
		OfferDto offerDto = new OfferDto(Constants.VALID_OFFER_TITLE_JAVA_BACKEND, new Date(), Constants.ZERO);
		this.offerService.createOffer(offerDto);
	}
	
	@Test
	public void whenTitleValid_userShouldBeAbleToReadOffer() throws NoContentException {
		OfferDto offerDto = this.offerService.readOffer(Constants.VALID_OFFER_TITLE_JAVA_BACKEND);
		assertEquals(offerDto.getJobTitle(), Constants.VALID_OFFER_TITLE_JAVA_BACKEND);
	}
	
	@Test(expected = NoContentException.class)
	public void whenTitleInValid_userShouldGetNoContentException() throws NoContentException {
		this.offerService.readOffer(Constants.INVALID_OFFER_TITLE_PYTHON);
	}
	
	@Test
	public void whenNoOfferPresnt_userShouldGetAnEmpltyList() {
		when(this.offerRepository.getOffers()).thenReturn(Collections.emptyList());
		
		List<OfferDto> offers = this.offerService.getOffers();
		assertEquals(offers.size(), Constants.ZERO);
		assertEquals(offers, Collections.emptyList());
	}
	
	@Test
	public void whenOfferPresnt_userShouldGetListofAllOffers() {
		List<OfferDto> offers = this.offerService.getOffers();
		assertEquals(offers.size(), Constants.ONE);
		assertEquals(offers.get(Constants.ZERO).getJobTitle(), Constants.VALID_OFFER_TITLE_JAVA_BACKEND);
		assertEquals(offers.get(Constants.ZERO).getNumberOfApplications(), new Integer(Constants.ZERO));
	}
	
	@Test
	public void whenOfferExists_userShouldGetTrue() {
		assertTrue(this.offerService.isOfferExists(Constants.VALID_OFFER_TITLE_JAVA_BACKEND));
	}
	
	@Test
	public void whenOfferDoesNotExists_userShouldGetFalse() {
		assertFalse(this.offerService.isOfferExists(Constants.INVALID_OFFER_TITLE_PYTHON));
	}
	
}
