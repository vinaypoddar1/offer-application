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

import com.heavenhr.dtos.ApplicationDto;
import com.heavenhr.entities.Application;
import com.heavenhr.entities.Offer;
import com.heavenhr.exceptions.InvalidApplicantInfoException;
import com.heavenhr.exceptions.InvalidOfferException;
import com.heavenhr.exceptions.NoContentException;
import com.heavenhr.repository.OfferRepository;
import com.heavenhr.services.ApplicationService;
import com.heavenhr.utils.Status;

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

	private static final String VALID_OFFER_TITLE_JAVA_BACKEND = "Java-Backends";
	private static final String INVALID_OFFER_TITLE_PYTHON = "Python";
	private static final String VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND = "paul@aol.com";
	private static final String INVALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND = "ted@gmail.com";
	private static final String SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND = "Sample Java Resume";

	private static final int ZERO = 0;
	private static final int ONE = 1;

	@Before
	public void setup() throws NoContentException {

		Application application = new Application(VALID_OFFER_TITLE_JAVA_BACKEND,
				VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND, SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND, Status.APPLIED);

		Offer offer = new Offer(VALID_OFFER_TITLE_JAVA_BACKEND, new Date());
		offer.getApplications().put(VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND, application);

		when(this.offerRepository.getOffer(VALID_OFFER_TITLE_JAVA_BACKEND)).thenReturn(Optional.of(offer));

		when(this.offerRepository.getOffers()).thenReturn(Arrays.asList(offer));
	}

	@Test
	public void whenApplicationIsValid_userShouldBeAbleToCreateAnApplication()
			throws InvalidOfferException, NoContentException {
		ApplicationDto applicationDto = new ApplicationDto(VALID_OFFER_TITLE_JAVA_BACKEND,
				VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND, SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND, Status.APPLIED);
		this.applicationService.createApplication(applicationDto);
		ApplicationDto application = this.applicationService.getApplication(VALID_OFFER_TITLE_JAVA_BACKEND,
				VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		assertEquals(application.getOfferTitle(), VALID_OFFER_TITLE_JAVA_BACKEND);
		assertEquals(application.getCandidateEmail(), VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		assertEquals(application.getResumeText(), SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND);
		assertEquals(application.getApplicationStatus(), Status.APPLIED);
	}

	@Test(expected = InvalidOfferException.class)
	public void whenOfferTitleNotValid_userShouldGetInvalidOfferException() throws InvalidOfferException {
		ApplicationDto applicationDto = new ApplicationDto(INVALID_OFFER_TITLE_PYTHON,
				VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND, SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND, Status.APPLIED);
		this.applicationService.createApplication(applicationDto);
	}

	@Test
	public void whenOfferTitleAndEmailIsValid_userShouldBeAbleToReadTheApplication()
			throws InvalidOfferException, NoContentException {
		ApplicationDto applicationDto = this.applicationService.getApplication(VALID_OFFER_TITLE_JAVA_BACKEND,
				VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		assertEquals(applicationDto.getOfferTitle(), VALID_OFFER_TITLE_JAVA_BACKEND);
		assertEquals(applicationDto.getCandidateEmail(), VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		assertEquals(applicationDto.getResumeText(), SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND);
		assertEquals(applicationDto.getApplicationStatus(), Status.APPLIED);
	}

	@Test(expected = InvalidOfferException.class)
	public void whenOfferTitleIsInValidAndEmailIsValid_userShouldGetInvalidOfferException()
			throws InvalidOfferException, NoContentException {
		this.applicationService.getApplication(INVALID_OFFER_TITLE_PYTHON, VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
	}

	@Test(expected = NoContentException.class)
	public void whenOfferTitleIsInValidAndEmailIsInValid_userShouldGetEmptyObject()
			throws InvalidOfferException, NoContentException {
		this.applicationService.getApplication(VALID_OFFER_TITLE_JAVA_BACKEND,
				INVALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
	}

	@Test
	public void whenOfferTitleValid_userShouldBeAbleToReadAllTheCorrespondingApplication()
			throws InvalidOfferException, NoContentException {
		List<ApplicationDto> applicationDtos = this.applicationService
				.getApplicationsByOffer(VALID_OFFER_TITLE_JAVA_BACKEND);
		assertEquals(applicationDtos.get(ZERO).getOfferTitle(), VALID_OFFER_TITLE_JAVA_BACKEND);
		assertEquals(applicationDtos.get(ZERO).getCandidateEmail(), VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		assertEquals(applicationDtos.get(ZERO).getResumeText(), SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND);
		assertEquals(applicationDtos.get(ZERO).getApplicationStatus(), Status.APPLIED);
	}

	@Test(expected = InvalidOfferException.class)
	public void whenOfferTitleIsInValid_userShouldGetInvalidOfferException()
			throws InvalidOfferException, NoContentException {
		this.applicationService.getApplicationsByOffer(INVALID_OFFER_TITLE_PYTHON);
	}

	@Test
	public void whenOfferTitleAndEmailIsValid_userShouldBeAbleToUpdateTheStatus()
			throws InvalidOfferException, InvalidApplicantInfoException, NoContentException {
		this.applicationService.updateApplicationStatus(VALID_OFFER_TITLE_JAVA_BACKEND,
				VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND, Status.HIRED);
		ApplicationDto applicationDto = this.applicationService.getApplication(VALID_OFFER_TITLE_JAVA_BACKEND,
				VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		assertEquals(applicationDto.getApplicationStatus(), Status.HIRED);
	}

	@Test(expected = InvalidOfferException.class)
	public void whenOfferTitleIsInValid_userShouldGetInvalidOfferException_whileUpdatingStatus()
			throws InvalidOfferException, InvalidApplicantInfoException {
		this.applicationService.updateApplicationStatus(INVALID_OFFER_TITLE_PYTHON,
				VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND, Status.HIRED);
	}

	@Test(expected = InvalidApplicantInfoException.class)
	public void whenOfferTitleIsValidAndEmailIsInValid_userShouldGetInvalidApplicantInfoException_whileUpdatingStatus()
			throws InvalidOfferException, InvalidApplicantInfoException {
		this.applicationService.updateApplicationStatus(VALID_OFFER_TITLE_JAVA_BACKEND,
				INVALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND, Status.HIRED);
	}

	@Test
	public void whenEmailIsRegistered_userShouldBeAbleToFindAllTheApplicationBasedOnTheEmail() {
		List<ApplicationDto> applicationDtos = this.applicationService
				.getApplicatonsByEmail(VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		assertEquals(applicationDtos.get(ZERO).getOfferTitle(), VALID_OFFER_TITLE_JAVA_BACKEND);
		assertEquals(applicationDtos.get(ZERO).getCandidateEmail(), VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		assertEquals(applicationDtos.get(ZERO).getResumeText(), SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND);
		assertEquals(applicationDtos.get(ZERO).getApplicationStatus(), Status.APPLIED);
	}

	@Test
	public void whenEmailIsNotRegistered_userShouldGetEmpltyList_whileFindingApplicationBasedOnTheEmail() {
		List<ApplicationDto> applicationDtos = this.applicationService
				.getApplicatonsByEmail(INVALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		assertEquals(applicationDtos, Collections.emptyList());
	}

	@Test
	public void whenOfferAndEmailValid_userShouldGetTrueOnApplicationExists() {
		assertTrue(this.applicationService.isApplicationsExists(VALID_OFFER_TITLE_JAVA_BACKEND,
				VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND));
	}

	@Test
	public void whenOfferInValidAndEmailValid_userShouldGetFalseOnApplicationExists() {
		assertFalse(this.applicationService.isApplicationsExists(INVALID_OFFER_TITLE_PYTHON,
				VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND));
	}

	@Test
	public void whenOfferValidAndEmailInValid_userShouldGetFalseOnApplicationExists() {
		assertFalse(this.applicationService.isApplicationsExists(VALID_OFFER_TITLE_JAVA_BACKEND,
				INVALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND));
	}

}
