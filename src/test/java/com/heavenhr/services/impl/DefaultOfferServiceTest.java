package com.heavenhr.services.impl;

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

import com.heavenhr.dtos.OfferDto;
import com.heavenhr.entities.Offer;
import com.heavenhr.exceptions.NoContentException;
import com.heavenhr.repository.OfferRepository;
import com.heavenhr.services.OfferService;

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
	
	private static final String VALID_OFFER_TITLE_JAVA_BACKEND = "Java-Backends";
	private static final String INVALID_OFFER_TITLE_PYTHON = "Python";
	private static final int ZERO = 0;
	private static final int ONE = 1;
	
	@Before
	public void setup() throws NoContentException {
		Offer offer = new Offer(VALID_OFFER_TITLE_JAVA_BACKEND, new Date());
		when(this.offerRepository.getOffer(VALID_OFFER_TITLE_JAVA_BACKEND))
			.thenReturn(Optional.of(offer));
		
		when(this.offerRepository.getOffers()).thenReturn(Arrays.asList(offer));
	}
	
	@Test
	public void whenTitleUnique_userShouldBeAbleToCreateOffer() {
		OfferDto offerDto = new OfferDto(VALID_OFFER_TITLE_JAVA_BACKEND, new Date(), ZERO);
		this.offerService.createOffer(offerDto);
	}
	
	@Test
	public void whenTitleValid_userShouldBeAbleToReadOffer() throws NoContentException {
		OfferDto offerDto = this.offerService.readOffer(VALID_OFFER_TITLE_JAVA_BACKEND);
		assertEquals(offerDto.getJobTitle(), VALID_OFFER_TITLE_JAVA_BACKEND);
	}
	
	@Test(expected = NoContentException.class)
	public void whenTitleInValid_userShouldGetNoContentException() throws NoContentException {
		this.offerService.readOffer(INVALID_OFFER_TITLE_PYTHON);
	}
	
	@Test
	public void whenNoOfferPresnt_userShouldGetAnEmpltyList() {
		when(this.offerRepository.getOffers()).thenReturn(Collections.emptyList());
		
		List<OfferDto> offers = this.offerService.getOffers();
		assertEquals(offers.size(), ZERO);
		assertEquals(offers, Collections.emptyList());
	}
	
	@Test
	public void whenOfferPresnt_userShouldGetListofAllOffers() {
		List<OfferDto> offers = this.offerService.getOffers();
		assertEquals(offers.size(), ONE);
		assertEquals(offers.get(ZERO).getJobTitle(), VALID_OFFER_TITLE_JAVA_BACKEND);
		assertEquals(offers.get(ZERO).getNumberOfApplications(), new Integer(ZERO));
	}
	
	@Test
	public void whenOfferExists_userShouldGetTrue() {
		assertTrue(this.offerService.isOfferExists(VALID_OFFER_TITLE_JAVA_BACKEND));
	}
	
	@Test
	public void whenOfferDoesNotExists_userShouldGetFalse() {
		assertFalse(this.offerService.isOfferExists(INVALID_OFFER_TITLE_PYTHON));
	}
	
}
