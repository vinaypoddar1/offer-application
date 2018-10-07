package com.heavenhr.humanresourceservice.controllers;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heavenhr.humanresourceservice.controllers.OfferController;
import com.heavenhr.humanresourceservice.dtos.ApplicationDto;
import com.heavenhr.humanresourceservice.dtos.CandidateDto;
import com.heavenhr.humanresourceservice.dtos.OfferDto;
import com.heavenhr.humanresourceservice.exceptions.InvalidApplicantInfoException;
import com.heavenhr.humanresourceservice.exceptions.InvalidOfferException;
import com.heavenhr.humanresourceservice.exceptions.NoContentException;
import com.heavenhr.humanresourceservice.services.ApplicationService;
import com.heavenhr.humanresourceservice.services.OfferService;
import com.heavenhr.utils.constants.Constants;
import com.heavenhr.utils.enums.Status;


@RunWith(SpringRunner.class)
@WebMvcTest(OfferController.class)
public class OfferControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private OfferService offerService;

	@MockBean
	private ApplicationService applicationService;

	ObjectMapper mapper = new ObjectMapper();

	@Test
	public void createOffer_whenValidInput_thenCreated() throws Exception {

		OfferDto javaBackend = new OfferDto(Constants.VALID_OFFER_TITLE_JAVA_BACKEND, new Date(), 2);

		mvc.perform(post("/heavenHR/v1/offers").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(javaBackend))).andExpect(status().isCreated());
	}

	@Test
	public void createOffer_whenBlankTitleInput_thenBadRequest() throws Exception {

		OfferDto javaBackend = new OfferDto(Constants.BLANK_STRING, new Date(), 2);

		mvc.perform(post("/heavenHR/v1/offers").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(javaBackend))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is(Constants.BAD_REQUEST))).andExpect(jsonPath("$.code", is(Constants.FOUR_HUNDRED)))
				.andExpect(jsonPath("$.message", is(Constants.TITLE_MUST_NOT_BE_BLANK)));
	}

	@Test
	public void createOffer_whenDuplicateTitleInput_thenBadRequest() throws Exception {

		given(this.offerService.isOfferExists(Constants.VALID_OFFER_TITLE_JAVA_BACKEND)).willReturn(true);

		OfferDto javaBackend = new OfferDto(Constants.VALID_OFFER_TITLE_JAVA_BACKEND, new Date(), 2);

		mvc.perform(post("/heavenHR/v1/offers").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(javaBackend))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is(Constants.BAD_REQUEST))).andExpect(jsonPath("$.code", is(Constants.FOUR_HUNDRED)))
				.andExpect(jsonPath("$.message", is(Constants.DUPLICATE_OFFER_TITLE)));
	}

	@Test
	public void getOffers_whenList_thenOk() throws Exception {

		OfferDto javaBackend = new OfferDto(Constants.VALID_OFFER_TITLE_JAVA_BACKEND, new Date(), 2);

		List<OfferDto> allEmployees = Arrays.asList(javaBackend);

		given(this.offerService.getOffers()).willReturn(allEmployees);

		mvc.perform(get("/heavenHR/v1/offers").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].jobTitle", is(javaBackend.getJobTitle())))
				// .andExpect(jsonPath("$[0].startDate", is(javaBackend.getStartDate())))
				.andExpect(jsonPath("$[0].numberOfApplications", is(javaBackend.getNumberOfApplications())));
	}
	
	@Test
	public void getOffers_whenEmpltyList_thenOk() throws Exception {

		given(this.offerService.getOffers()).willReturn(Collections.emptyList());

		mvc.perform(get("/heavenHR/v1/offers").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(Constants.ZERO)))
				 .andExpect(jsonPath("$", is(Collections.emptyList())));
	}

	@Test
	public void readSingleOffer_whenNoOfferPresent_thenNotFound() throws Exception {

		given(this.offerService.readOffer(Constants.INVALID_OFFER_TITLE_PYTHON))
				.willThrow(new NoContentException(Constants.NO_OFFER_AVAILABLE_WITH_NAME + Constants.INVALID_OFFER_TITLE_PYTHON));

		mvc.perform(get("/heavenHR/v1/offers/" + Constants.INVALID_OFFER_TITLE_PYTHON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$.status", is(Constants.NOT_FOUND)))
				.andExpect(jsonPath("$.code", is(Constants.FOUR_HUNDRED_FOUR)))
				.andExpect(jsonPath("$.message", is(Constants.NO_OFFER_AVAILABLE_WITH_NAME + Constants.INVALID_OFFER_TITLE_PYTHON)));
	}

	@Test
	public void readSingleOffer_whenOfferPresent_thenOK() throws Exception {

		OfferDto offer = new OfferDto(Constants.INVALID_OFFER_TITLE_PYTHON, new Date(), 3);
		given(this.offerService.readOffer(Constants.INVALID_OFFER_TITLE_PYTHON)).willReturn(offer);

		mvc.perform(get("/heavenHR/v1/offers/" + Constants.INVALID_OFFER_TITLE_PYTHON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.jobTitle", is(offer.getJobTitle())))
				// .andExpect(jsonPath("$.startDate", is(offer.getStartDate())))
				.andExpect(jsonPath("$.numberOfApplications", is(offer.getNumberOfApplications())));
	}

	@Test
	public void applyOffer_whenValidParameters_thenOK() throws Exception {

		ApplicationDto application = new ApplicationDto();
		application.setOffer(Constants.VALID_OFFER_TITLE_JAVA_BACKEND);
		application.setCandidateEmail(Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		application.setResumeText(Constants.SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND);

		mvc.perform(post("/heavenHR/v1/offers/" + Constants.VALID_OFFER_TITLE_JAVA_BACKEND + "/applications")
				.content(mapper.writeValueAsBytes(application)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	public void applyOffer_whenWrongEmail_thenBadRequest() throws Exception {

		ApplicationDto application = new ApplicationDto();
		application.setOffer(Constants.VALID_OFFER_TITLE_JAVA_BACKEND);
		application.setCandidateEmail(Constants.INVALID_APPLICATION_EMAIL_FORMAT);
		application.setResumeText(Constants.SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND);

		mvc.perform(post("/heavenHR/v1/offers/" + Constants.VALID_OFFER_TITLE_JAVA_BACKEND + "/applications")
				.content(mapper.writeValueAsBytes(application)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is(Constants.EMAIL_FORMAT_NOT_CORRECT)));
	}

	@Test
	public void applyOffer_whenBlankEmail_thenBadRequest() throws Exception {

		ApplicationDto application = new ApplicationDto();
		application.setOffer(Constants.VALID_OFFER_TITLE_JAVA_BACKEND);
		// application.setCandidateEmail(null);
		application.setCandidateEmail(Constants.BLANK_STRING);
		application.setResumeText(Constants.SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND);

		mvc.perform(post("/heavenHR/v1/offers/" + Constants.VALID_OFFER_TITLE_JAVA_BACKEND + "/applications")
				.content(mapper.writeValueAsBytes(application)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is(Constants.EMAIL_MUST_MOT_BE_BLANK)));
	}

	// @Test
	public void applyOffer_whenOfferInValid_thenInvalidOfferException() throws Exception {

		ApplicationDto application = new ApplicationDto();
		application.setOffer(Constants.VALID_OFFER_TITLE_JAVA_BACKEND);
		application.setCandidateEmail(Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		application.setResumeText(Constants.SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND);

		doThrow(new InvalidOfferException(Constants.INVALID_OFFER_TITLE + Constants.VALID_OFFER_TITLE_JAVA_BACKEND)).when(this.applicationService)
				.createApplication(application);

		mvc.perform(post("/heavenHR/v1/offers/" + Constants.VALID_OFFER_TITLE_JAVA_BACKEND + "/applications")
				.content(mapper.writeValueAsBytes(application)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is(Constants.INVALID_OFFER_TITLE + Constants.VALID_OFFER_TITLE_JAVA_BACKEND)));
	}

	// @Test
	public void applyOffer_whenApplicationNotUnique_thenBadRequest() throws Exception {

		ApplicationDto application = new ApplicationDto();
		application.setOffer(Constants.VALID_OFFER_TITLE_JAVA_BACKEND);
		application.setCandidateEmail(Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		application.setResumeText(Constants.SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND);

		given(this.applicationService.isApplicationsExists(Constants.VALID_OFFER_TITLE_JAVA_BACKEND, application.getCandidateEmail()))
				.willReturn(true);

		mvc.perform(post("/heavenHR/v1/offers/" + Constants.VALID_OFFER_TITLE_JAVA_BACKEND + "/applications")
				.content(mapper.writeValueAsBytes(application)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is(Constants.EMAIL_MUST_MOT_BE_BLANK)));
	}

	@Test
	public void getAllApplications_whenOfferValid_thenOk() throws Exception {

		ApplicationDto application = new ApplicationDto();
		application.setOffer(Constants.VALID_OFFER_TITLE_JAVA_BACKEND);
		application.setCandidateEmail(Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		application.setResumeText(Constants.SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND);

		given(this.applicationService.getApplicationsByOffer(Constants.VALID_OFFER_TITLE_JAVA_BACKEND)).willReturn(Arrays.asList(application));

		mvc.perform(get("/heavenHR/v1/offers/" + Constants.VALID_OFFER_TITLE_JAVA_BACKEND + "/applications").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(Constants.ONE)))
				.andExpect(jsonPath("$[0].offer", is(application.getOffer())))
				.andExpect(jsonPath("$[0].candidateEmail", is(application.getCandidateEmail())))
				.andExpect(jsonPath("$[0].resumeText", is(application.getResumeText())))
				.andExpect(jsonPath("$[0].applicationStatus", is(application.getApplicationStatus().toString())));
	}

	@Test
	public void getAllApplications_whenOfferInValid_thenOk() throws Exception {

		ApplicationDto application = new ApplicationDto();
		application.setOffer(Constants.VALID_OFFER_TITLE_JAVA_BACKEND);
		application.setCandidateEmail(Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		application.setResumeText(Constants.SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND);

		given(this.applicationService.getApplicationsByOffer(Constants.VALID_OFFER_TITLE_JAVA_BACKEND))
				.willThrow(new InvalidOfferException(Constants.INVALID_OFFER_TITLE + Constants.VALID_OFFER_TITLE_JAVA_BACKEND));

		mvc.perform(get("/heavenHR/v1/offers/" + Constants.VALID_OFFER_TITLE_JAVA_BACKEND + "/applications").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status", is(Constants.BAD_REQUEST)))
				.andExpect(jsonPath("$.code", is(Constants.FOUR_HUNDRED)))
				.andExpect(jsonPath("$.message", is(Constants.INVALID_OFFER_TITLE + Constants.VALID_OFFER_TITLE_JAVA_BACKEND)));
	}

	@Test
	public void getApplication_whenOfferValid_thenOk() throws Exception {

		ApplicationDto application = new ApplicationDto();
		application.setOffer(Constants.VALID_OFFER_TITLE_JAVA_BACKEND);
		application.setCandidateEmail(Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		application.setResumeText(Constants.SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND);

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		given(this.applicationService.getApplication(Constants.VALID_OFFER_TITLE_JAVA_BACKEND, Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND)).willReturn(application);

		mvc.perform(post("/heavenHR/v1/offers/" + Constants.VALID_OFFER_TITLE_JAVA_BACKEND + "/applications/track")
				.content(mapper.writeValueAsBytes(candidate)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.offer", is(application.getOffer())))
				.andExpect(jsonPath("$.candidateEmail", is(application.getCandidateEmail())))
				.andExpect(jsonPath("$.resumeText", is(application.getResumeText())))
				.andExpect(jsonPath("$.applicationStatus", is(application.getApplicationStatus().toString())));
	}

	@Test
	public void getApplication_whenEmailBlank_thenOk() throws Exception {

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(Constants.BLANK_STRING);

		mvc.perform(post("/heavenHR/v1/offers/" + Constants.VALID_OFFER_TITLE_JAVA_BACKEND + "/applications/track")
				.content(mapper.writeValueAsBytes(candidate)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status", is(Constants.BAD_REQUEST)))
				.andExpect(jsonPath("$.code", is(Constants.FOUR_HUNDRED))).andExpect(jsonPath("$.message", is(Constants.EMAIL_MUST_MOT_BE_BLANK)));
	}

	@Test
	public void getApplication_whenEmailInValid_thenBadRequest() throws Exception {

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(Constants.INVALID_APPLICATION_EMAIL_FORMAT);

		mvc.perform(post("/heavenHR/v1/offers/" + Constants.VALID_OFFER_TITLE_JAVA_BACKEND + "/applications/track")
				.content(mapper.writeValueAsBytes(candidate)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status", is(Constants.BAD_REQUEST)))
				.andExpect(jsonPath("$.code", is(Constants.FOUR_HUNDRED)))
				.andExpect(jsonPath("$.message", is(Constants.EMAIL_FORMAT_NOT_CORRECT)));
	}

	@Test
	public void updateApplicationStatus_whenEmailValid_thenOk() throws Exception {

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		candidate.setStatus(Status.HIRED);

		mvc.perform(put("/heavenHR/v1/offers/" + Constants.VALID_OFFER_TITLE_JAVA_BACKEND + "/applications/update")
				.content(mapper.writeValueAsBytes(candidate)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void updateApplicatioStatus_whenOfferInValid_thenBadRequest() throws Exception {

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		candidate.setStatus(Status.HIRED);

		doThrow(new InvalidOfferException(Constants.INVALID_OFFER_TITLE + Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND)).when(this.applicationService)
				.updateApplicationStatus(Constants.VALID_OFFER_TITLE_JAVA_BACKEND, Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND, Status.HIRED);

		mvc.perform(put("/heavenHR/v1/offers/" + Constants.VALID_OFFER_TITLE_JAVA_BACKEND + "/applications/update")
				.content(mapper.writeValueAsBytes(candidate)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status", is(Constants.BAD_REQUEST)))
				.andExpect(jsonPath("$.code", is(Constants.FOUR_HUNDRED)))
				.andExpect(jsonPath("$.message", is(Constants.INVALID_OFFER_TITLE + Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND)));
	}

	@Test
	public void updateApplicatioStatus_whenEmailValidButNotRegistered_thenBadRequest() throws Exception {

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);
		candidate.setStatus(Status.HIRED);

		doThrow(new InvalidApplicantInfoException(Constants.INVALID_EMAIL_FOR_APPLICANT + Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND)).when(this.applicationService)
				.updateApplicationStatus(Constants.VALID_OFFER_TITLE_JAVA_BACKEND, Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND, Status.HIRED);

		mvc.perform(put("/heavenHR/v1/offers/" + Constants.VALID_OFFER_TITLE_JAVA_BACKEND + "/applications/update")
				.content(mapper.writeValueAsBytes(candidate)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status", is(Constants.BAD_REQUEST)))
				.andExpect(jsonPath("$.code", is(Constants.FOUR_HUNDRED)))
				.andExpect(jsonPath("$.message", is(Constants.INVALID_EMAIL_FOR_APPLICANT + Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND)));
	}

	@Test
	public void updateApplicatioStatus_whenEmailBlank_thenBadRequest() throws Exception {

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(Constants.BLANK_STRING);
		candidate.setStatus(Status.HIRED);

		mvc.perform(put("/heavenHR/v1/offers/" + Constants.VALID_OFFER_TITLE_JAVA_BACKEND + "/applications/update")
				.content(mapper.writeValueAsBytes(candidate)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status", is(Constants.BAD_REQUEST)))
				.andExpect(jsonPath("$.code", is(Constants.FOUR_HUNDRED))).andExpect(jsonPath("$.message", is(Constants.EMAIL_MUST_MOT_BE_BLANK)));
	}

	@Test
	public void updateApplicationStatus_whenEmailInValid_thenBadRequest() throws Exception {

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(Constants.INVALID_APPLICATION_EMAIL_FORMAT);
		candidate.setStatus(Status.HIRED);

		mvc.perform(put("/heavenHR/v1/offers/" + Constants.VALID_OFFER_TITLE_JAVA_BACKEND + "/applications/update")
				.content(mapper.writeValueAsBytes(candidate)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status", is(Constants.BAD_REQUEST)))
				.andExpect(jsonPath("$.code", is(Constants.FOUR_HUNDRED)))
				.andExpect(jsonPath("$.message", is(Constants.EMAIL_FORMAT_NOT_CORRECT)));
	}

}
