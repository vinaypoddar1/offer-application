package com.heavenhr.controllers;

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
import com.heavenhr.controllers.ApplicationController;
import com.heavenhr.dtos.ApplicationDto;
import com.heavenhr.dtos.CandidateDto;
import com.heavenhr.services.ApplicationService;
import com.heavenhr.utils.Status;

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

		String OfferTitle = "Java-Backend";
		String email = "paul@aol.com";
		String resumeText = "Sample Java Resume";

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(email);

		ApplicationDto application = new ApplicationDto(OfferTitle, email, resumeText, Status.HIRED);

		given(this.applicationService.getApplicatonsByEmail(email)).willReturn(Arrays.asList(application));

		mvc.perform(post("/heavenHR/v1/applications/email").content(mapper.writeValueAsBytes(candidate))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].candidateEmail", is(application.getCandidateEmail())))
				.andExpect(jsonPath("$[0].offerTitle", is(application.getOfferTitle())))
				.andExpect(jsonPath("$[0].resumeText", is(application.getResumeText())))
				.andExpect(jsonPath("$[0].applicationStatus", is(application.getApplicationStatus().toString())));
	}

	@Test
	public void getApplicationsByEmail_whenEmailValidButNotRegistered_thenOk() throws Exception {

		String email = "paul@aol.com";

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(email);

		given(this.applicationService.getApplicatonsByEmail(email)).willReturn(Collections.emptyList());

		mvc.perform(post("/heavenHR/v1/applications/email").content(mapper.writeValueAsBytes(candidate))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0))).andExpect(jsonPath("$", is(Collections.emptyList())));
	}

	@Test
	public void getApplicationsByEmail_whenEmailBlank_thenBadRequest() throws Exception {

		String email = "";

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(email);

		mvc.perform(post("/heavenHR/v1/applications/email").content(mapper.writeValueAsBytes(candidate))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is("Bad Request"))).andExpect(jsonPath("$.code", is(400)))
				.andExpect(jsonPath("$.message", is("Email must mot be blank")));
	}

	@Test
	public void getApplicationsByEmail_whenEmailInValid_thenBadRequest() throws Exception {

		String email = "abc123";

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(email);

		mvc.perform(post("/heavenHR/v1/applications/email").content(mapper.writeValueAsBytes(candidate))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is("Bad Request"))).andExpect(jsonPath("$.code", is(400)))
				.andExpect(jsonPath("$.message", is("Email format not correct")));
	}

}
