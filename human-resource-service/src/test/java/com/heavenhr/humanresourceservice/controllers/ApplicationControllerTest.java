package com.heavenhr.humanresourceservice.controllers;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heavenhr.humanresourceservice.controllers.ApplicationController;
import com.heavenhr.humanresourceservice.dtos.ApplicationDto;
import com.heavenhr.humanresourceservice.dtos.CandidateDto;
import com.heavenhr.humanresourceservice.services.ApplicationService;
import com.heavenhr.utils.constants.Constants;
import com.heavenhr.utils.enums.Status;


@RunWith(SpringRunner.class)
@WebMvcTest(ApplicationController.class)
public class ApplicationControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ApplicationService applicationService;

	ObjectMapper mapper = new ObjectMapper();

	@Test
	public void getApplicationsByEmail_whenEmailValid_thenOk() throws Exception {

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);

		ApplicationDto application = new ApplicationDto(Constants.VALID_OFFER_TITLE_JAVA_BACKEND, Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND, Constants.SAMPLE_RESUME_TEXT_FOR_JAVA_BACKEND, Status.HIRED);

		given(this.applicationService.getApplicatonsByEmail(Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND)).willReturn(Arrays.asList(application));

		mvc.perform(post("/heavenHR/v1/applications/email").content(mapper.writeValueAsBytes(candidate))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].candidateEmail", is(application.getCandidateEmail())))
				.andExpect(jsonPath("$[0].offer", is(application.getOffer())))
				.andExpect(jsonPath("$[0].resumeText", is(application.getResumeText())))
				.andExpect(jsonPath("$[0].applicationStatus", is(application.getApplicationStatus().toString())));
	}

	@Test
	public void getApplicationsByEmail_whenEmailValidButNotRegistered_thenOk() throws Exception {


		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND);

		given(this.applicationService.getApplicatonsByEmail(Constants.VALID_APPLICATION_EMAIL_FOR_JAVA_BACKEND)).willReturn(Collections.emptyList());

		mvc.perform(post("/heavenHR/v1/applications/email").content(mapper.writeValueAsBytes(candidate))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0))).andExpect(jsonPath("$", is(Collections.emptyList())));
	}

	@Test
	public void getApplicationsByEmail_whenEmailBlank_thenBadRequest() throws Exception {

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(Constants.BLANK_STRING);

		mvc.perform(post("/heavenHR/v1/applications/email").content(mapper.writeValueAsBytes(candidate))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is(Constants.BAD_REQUEST))).andExpect(jsonPath("$.code", is(Constants.FOUR_HUNDRED)))
				.andExpect(jsonPath("$.message", is(Constants.EMAIL_MUST_MOT_BE_BLANK)));
	}

	@Test
	public void getApplicationsByEmail_whenEmailInValid_thenBadRequest() throws Exception {

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(Constants.INVALID_APPLICATION_EMAIL_FORMAT);

		mvc.perform(post("/heavenHR/v1/applications/email").content(mapper.writeValueAsBytes(candidate))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is(Constants.BAD_REQUEST))).andExpect(jsonPath("$.code", is(Constants.FOUR_HUNDRED)))
				.andExpect(jsonPath("$.message", is(Constants.EMAIL_FORMAT_NOT_CORRECT)));
	}

}
