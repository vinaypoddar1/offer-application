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

import com.heavenhr.humanresourceservice.dtos.ApplicationDto;
import com.heavenhr.humanresourceservice.entities.Application;
import com.heavenhr.humanresourceservice.entities.Offer;
import com.heavenhr.humanresourceservice.exceptions.InvalidApplicantInfoException;
import com.heavenhr.humanresourceservice.exceptions.InvalidOfferException;
import com.heavenhr.humanresourceservice.exceptions.NoContentException;
import com.heavenhr.humanresourceservice.repositories.OfferRepository;
import com.heavenhr.humanresourceservice.services.ApplicationService;
import com.heavenhr.utils.constants.Constants;
import com.heavenhr.utils.enums.Status;

@RunWith(SpringRunner.class)
public class DefaultApplicationServiceTest {

	@TestConfiguration
	static class DefaultApplicationServiceTestContextConfiguration {

		@Bean
		public ApplicationService applicationService() {
			return new DefaultApplicationService();
		}
	}

	@Autowired
	private ApplicationService applicationService;

	@MockBean
	private OfferRepository offerRepository;

	@Before
	public void setup() throws NoContentException {

		Application application = new Application(Constants.VALID_OFFER_TITLE_JAVA_BACKEND  ,
				Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND, Constants.SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND, Status.APPLIED);

		Offer offer = new Offer(Constants.VALID_OFFER_TITLE_JAVA_BACKEND  , new Date());
		offer.getApplications().put(Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND, application);

		when(this.offerRepository.getOffer(Constants.VALID_OFFER_TITLE_JAVA_BACKEND  )).thenReturn(Optional.of(offer));

		when(this.offerRepository.getOffers()).thenReturn(Arrays.asList(offer));
	}

	@Test
	public void whenApplicationIsValid_userShouldBeAbleToCreateAnApplication()
			throws InvalidOfferException, NoContentException {
		ApplicationDto applicationDto = new ApplicationDto(Constants.VALID_OFFER_TITLE_JAVA_BACKEND  ,
				Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND, Constants.SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND, Status.APPLIED);
		this.applicationService.createApplication(applicationDto);
		ApplicationDto application = this.applicationService.getApplication(Constants.VALID_OFFER_TITLE_JAVA_BACKEND  ,
				Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		assertEquals(application.getOffer(), Constants.VALID_OFFER_TITLE_JAVA_BACKEND  );
		assertEquals(application.getCandidateEmail(), Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		assertEquals(application.getResumeText(), Constants.SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND);
		assertEquals(application.getApplicationStatus(), Status.APPLIED);
	}

	@Test(expected = InvalidOfferException.class)
	public void whenOfferTitleNotValid_userShouldGetInvalidOfferException() throws InvalidOfferException {
		ApplicationDto applicationDto = new ApplicationDto(Constants.INVALID_OFFER_TITLE_PYTHON,
				Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND, Constants.SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND, Status.APPLIED);
		this.applicationService.createApplication(applicationDto);
	}

	@Test
	public void whenOfferTitleAndEmailIsValid_userShouldBeAbleToReadTheApplication()
			throws InvalidOfferException, NoContentException {
		ApplicationDto applicationDto = this.applicationService.getApplication(Constants.VALID_OFFER_TITLE_JAVA_BACKEND  ,
				Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		assertEquals(applicationDto.getOffer(), Constants.VALID_OFFER_TITLE_JAVA_BACKEND  );
		assertEquals(applicationDto.getCandidateEmail(), Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		assertEquals(applicationDto.getResumeText(), Constants.SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND);
		assertEquals(applicationDto.getApplicationStatus(), Status.APPLIED);
	}

	@Test(expected = InvalidOfferException.class)
	public void whenOfferTitleIsInValidAndEmailIsValid_userShouldGetInvalidOfferException()
			throws InvalidOfferException, NoContentException {
		this.applicationService.getApplication(Constants.INVALID_OFFER_TITLE_PYTHON, Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
	}

	@Test(expected = NoContentException.class)
	public void whenOfferTitleIsInValidAndEmailIsInValid_userShouldGetEmptyObject()
			throws InvalidOfferException, NoContentException {
		this.applicationService.getApplication(Constants.VALID_OFFER_TITLE_JAVA_BACKEND  ,
				Constants.INVALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
	}

	@Test
	public void whenOfferTitleValid_userShouldBeAbleToReadAllTheCorrespondingApplication()
			throws InvalidOfferException, NoContentException {
		List<ApplicationDto> applicationDtos = this.applicationService
				.getApplicationsByOffer(Constants.VALID_OFFER_TITLE_JAVA_BACKEND  );
		assertEquals(applicationDtos.get(Constants.ZERO).getOffer(), Constants.VALID_OFFER_TITLE_JAVA_BACKEND  );
		assertEquals(applicationDtos.get(Constants.ZERO).getCandidateEmail(), Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		assertEquals(applicationDtos.get(Constants.ZERO).getResumeText(), Constants.SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND);
		assertEquals(applicationDtos.get(Constants.ZERO).getApplicationStatus(), Status.APPLIED);
	}

	@Test(expected = InvalidOfferException.class)
	public void whenOfferTitleIsInValid_userShouldGetInvalidOfferException()
			throws InvalidOfferException, NoContentException {
		this.applicationService.getApplicationsByOffer(Constants.INVALID_OFFER_TITLE_PYTHON);
	}

	@Test
	public void whenOfferTitleAndEmailIsValid_userShouldBeAbleToUpdateTheStatus()
			throws InvalidOfferException, InvalidApplicantInfoException, NoContentException {
		this.applicationService.updateApplicationStatus(Constants.VALID_OFFER_TITLE_JAVA_BACKEND  ,
				Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND, Status.HIRED);
		ApplicationDto applicationDto = this.applicationService.getApplication(Constants.VALID_OFFER_TITLE_JAVA_BACKEND  ,
				Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		assertEquals(applicationDto.getApplicationStatus(), Status.HIRED);
	}

	@Test(expected = InvalidOfferException.class)
	public void whenOfferTitleIsInValid_userShouldGetInvalidOfferException_whileUpdatingStatus()
			throws InvalidOfferException, InvalidApplicantInfoException {
		this.applicationService.updateApplicationStatus(Constants.INVALID_OFFER_TITLE_PYTHON,
				Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND, Status.HIRED);
	}

	@Test(expected = InvalidApplicantInfoException.class)
	public void whenOfferTitleIsValidAndEmailIsInValid_userShouldGetInvalidApplicantInfoException_whileUpdatingStatus()
			throws InvalidOfferException, InvalidApplicantInfoException {
		this.applicationService.updateApplicationStatus(Constants.VALID_OFFER_TITLE_JAVA_BACKEND  ,
				Constants.INVALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND, Status.HIRED);
	}

	@Test
	public void whenEmailIsRegistered_userShouldBeAbleToFindAllTheApplicationBasedOnTheEmail() {
		List<ApplicationDto> applicationDtos = this.applicationService
				.getApplicatonsByEmail(Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		assertEquals(applicationDtos.get(Constants.ZERO).getOffer(), Constants.VALID_OFFER_TITLE_JAVA_BACKEND  );
		assertEquals(applicationDtos.get(Constants.ZERO).getCandidateEmail(), Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		assertEquals(applicationDtos.get(Constants.ZERO).getResumeText(), Constants.SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND);
		assertEquals(applicationDtos.get(Constants.ZERO).getApplicationStatus(), Status.APPLIED);
	}

	@Test
	public void whenEmailIsNotRegistered_userShouldGetEmpltyList_whileFindingApplicationBasedOnTheEmail() {
		List<ApplicationDto> applicationDtos = this.applicationService
				.getApplicatonsByEmail(Constants.INVALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		assertEquals(applicationDtos, Collections.emptyList());
	}

	@Test
	public void whenOfferAndEmailValid_userShouldGetTrueOnApplicationExists() {
		assertTrue(this.applicationService.isApplicationsExists(Constants.VALID_OFFER_TITLE_JAVA_BACKEND  ,
				Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND));
	}

	@Test
	public void whenOfferInValidAndEmailValid_userShouldGetFalseOnApplicationExists() {
		assertFalse(this.applicationService.isApplicationsExists(Constants.INVALID_OFFER_TITLE_PYTHON,
				Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND));
	}

	@Test
	public void whenOfferValidAndEmailInValid_userShouldGetFalseOnApplicationExists() {
		assertFalse(this.applicationService.isApplicationsExists(Constants.VALID_OFFER_TITLE_JAVA_BACKEND  ,
				Constants.INVALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND));
	}

}
