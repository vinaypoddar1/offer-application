package com.heavenhr.controllers;

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
import com.heavenhr.controllers.OfferController;
import com.heavenhr.dtos.ApplicationDto;
import com.heavenhr.dtos.CandidateDto;
import com.heavenhr.dtos.OfferDto;
import com.heavenhr.exceptions.InvalidApplicantInfoException;
import com.heavenhr.exceptions.InvalidOfferException;
import com.heavenhr.exceptions.NoContentException;
import com.heavenhr.services.ApplicationService;
import com.heavenhr.services.OfferService;
import com.heavenhr.utils.Status;

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

		OfferDto javaBackend = new OfferDto("Java-Backend", new Date(), 2);

		mvc.perform(post("/heavenHR/v1/offers").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(javaBackend))).andExpect(status().isCreated());
	}

	@Test
	public void createOffer_whenBlankTitleInput_thenBadRequest() throws Exception {

		OfferDto javaBackend = new OfferDto("", new Date(), 2);

		mvc.perform(post("/heavenHR/v1/offers").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(javaBackend))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is("Bad Request"))).andExpect(jsonPath("$.code", is(400)))
				.andExpect(jsonPath("$.message", is("Title must not be blank")));
	}

	@Test
	public void createOffer_whenDuplicateTitleInput_thenBadRequest() throws Exception {

		given(this.offerService.isOfferExists("Java-Backend")).willReturn(true);

		OfferDto javaBackend = new OfferDto("Java-Backend", new Date(), 2);

		mvc.perform(post("/heavenHR/v1/offers").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(javaBackend))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is("Bad Request"))).andExpect(jsonPath("$.code", is(400)))
				.andExpect(jsonPath("$.message", is("Duplicate Offer Title")));
	}

	@Test
	public void getOffers_whenList_thenOk() throws Exception {

		OfferDto javaBackend = new OfferDto("Java-Backend", new Date(), 2);

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
				.andExpect(jsonPath("$", hasSize(0)))
				 .andExpect(jsonPath("$", is(Collections.emptyList())));
	}

	@Test
	public void readSingleOffer_whenNoOfferPresent_thenNotFound() throws Exception {

		String offerTitle = "Python";
		given(this.offerService.readOffer(offerTitle))
				.willThrow(new NoContentException("No Offer available with name " + offerTitle));

		mvc.perform(get("/heavenHR/v1/offers/" + offerTitle).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$.status", is("Not Found")))
				.andExpect(jsonPath("$.code", is(404)))
				.andExpect(jsonPath("$.message", is("No Offer available with name " + offerTitle)));
	}

	@Test
	public void readSingleOffer_whenOfferPresent_thenOK() throws Exception {

		String offerTitle = "Python";
		OfferDto offer = new OfferDto(offerTitle, new Date(), 3);
		given(this.offerService.readOffer(offerTitle)).willReturn(offer);

		mvc.perform(get("/heavenHR/v1/offers/" + offerTitle).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.jobTitle", is(offer.getJobTitle())))
				// .andExpect(jsonPath("$.startDate", is(offer.getStartDate())))
				.andExpect(jsonPath("$.numberOfApplications", is(offer.getNumberOfApplications())));
	}

	@Test
	public void applyOffer_whenValidParameters_thenOK() throws Exception {

		String offerTitle = "Java-Backend";
		ApplicationDto application = new ApplicationDto();
		application.setOffer(offerTitle);
		application.setCandidateEmail("paul@aol.com");
		application.setResumeText("Sample Java Resume");

		mvc.perform(post("/heavenHR/v1/offers/" + offerTitle + "/applications")
				.content(mapper.writeValueAsBytes(application)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	public void applyOffer_whenWrongEmail_thenBadRequest() throws Exception {

		String offerTitle = "Java-Backend";
		ApplicationDto application = new ApplicationDto();
		application.setOffer(offerTitle);
		application.setCandidateEmail("wrongemailformat");
		application.setResumeText("Sample Java Resume");

		mvc.perform(post("/heavenHR/v1/offers/" + offerTitle + "/applications")
				.content(mapper.writeValueAsBytes(application)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is("Email format not correct")));
	}

	@Test
	public void applyOffer_whenBlankEmail_thenBadRequest() throws Exception {

		String offerTitle = "Java-Backend";
		ApplicationDto application = new ApplicationDto();
		application.setOffer(offerTitle);
		// application.setCandidateEmail(null);
		application.setCandidateEmail("");
		application.setResumeText("Sample Java Resume");

		mvc.perform(post("/heavenHR/v1/offers/" + offerTitle + "/applications")
				.content(mapper.writeValueAsBytes(application)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is("Email must mot be blank")));
	}

	// @Test
	public void applyOffer_whenOfferInValid_thenInvalidOfferException() throws Exception {

		String offerTitle = "Java-Backend";
		ApplicationDto application = new ApplicationDto();
		application.setOffer(offerTitle);
		application.setCandidateEmail("paul@aol.com");
		application.setResumeText("Sample Java Resume");

		doThrow(new InvalidOfferException("Invalid offer title " + offerTitle)).when(this.applicationService)
				.createApplication(application);

		mvc.perform(post("/heavenHR/v1/offers/" + offerTitle + "/applications")
				.content(mapper.writeValueAsBytes(application)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Invalid offer title " + offerTitle)));
	}

	// @Test
	public void applyOffer_whenApplicationNotUnique_thenBadRequest() throws Exception {

		String offerTitle = "Java-Backend";
		ApplicationDto application = new ApplicationDto();
		application.setOffer(offerTitle);
		application.setCandidateEmail("paul@aol.com");
		application.setResumeText("Sample Java Resume");

		given(this.applicationService.isApplicationsExists(offerTitle, application.getCandidateEmail()))
				.willReturn(true);

		mvc.perform(post("/heavenHR/v1/offers/" + offerTitle + "/applications")
				.content(mapper.writeValueAsBytes(application)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is("Email must mot be blank")));
	}

	@Test
	public void getAllApplications_whenOfferValid_thenOk() throws Exception {

		String offerTitle = "Java-Backend";
		ApplicationDto application = new ApplicationDto();
		application.setOffer(offerTitle);
		application.setCandidateEmail("paul@aol.com");
		application.setResumeText("Sample Java Resume");

		given(this.applicationService.getApplicationsByOffer(offerTitle)).willReturn(Arrays.asList(application));

		mvc.perform(get("/heavenHR/v1/offers/" + offerTitle + "/applications").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].offerTitle", is(application.getOfferTitle())))
				.andExpect(jsonPath("$[0].candidateEmail", is(application.getCandidateEmail())))
				.andExpect(jsonPath("$[0].resumeText", is(application.getResumeText())))
				.andExpect(jsonPath("$[0].applicationStatus", is(application.getApplicationStatus().toString())));
	}

	@Test
	public void getAllApplications_whenOfferInValid_thenOk() throws Exception {

		String offerTitle = "Java-Backend";
		ApplicationDto application = new ApplicationDto();
		application.setOffer(offerTitle);
		application.setCandidateEmail("paul@aol.com");
		application.setResumeText("Sample Java Resume");

		given(this.applicationService.getApplicationsByOffer(offerTitle))
				.willThrow(new InvalidOfferException("Invalid offer title " + offerTitle));

		mvc.perform(get("/heavenHR/v1/offers/" + offerTitle + "/applications").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status", is("Bad Request")))
				.andExpect(jsonPath("$.code", is(400)))
				.andExpect(jsonPath("$.message", is("Invalid offer title " + offerTitle)));
	}

	@Test
	public void getApplication_whenOfferValid_thenOk() throws Exception {

		String offerTitle = "Java-Backend";
		String email = "paul@aol.com";
		ApplicationDto application = new ApplicationDto();
		application.setOffer(offerTitle);
		application.setCandidateEmail(email);
		application.setResumeText("Sample Java Resume");

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(email);
		given(this.applicationService.getApplication(offerTitle, email)).willReturn(application);

		mvc.perform(post("/heavenHR/v1/offers/" + offerTitle + "/applications/track")
				.content(mapper.writeValueAsBytes(candidate)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.offerTitle", is(application.getOfferTitle())))
				.andExpect(jsonPath("$.candidateEmail", is(application.getCandidateEmail())))
				.andExpect(jsonPath("$.resumeText", is(application.getResumeText())))
				.andExpect(jsonPath("$.applicationStatus", is(application.getApplicationStatus().toString())));
	}

	@Test
	public void getApplication_whenEmailBlank_thenOk() throws Exception {

		String offerTitle = "Java-Backend";
		String email = "";

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(email);

		mvc.perform(post("/heavenHR/v1/offers/" + offerTitle + "/applications/track")
				.content(mapper.writeValueAsBytes(candidate)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status", is("Bad Request")))
				.andExpect(jsonPath("$.code", is(400))).andExpect(jsonPath("$.message", is("Email must mot be blank")));
	}

	@Test
	public void getApplication_whenEmailInValid_thenBadRequest() throws Exception {

		String offerTitle = "Java-Backend";
		String email = "abc123";

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(email);

		mvc.perform(post("/heavenHR/v1/offers/" + offerTitle + "/applications/track")
				.content(mapper.writeValueAsBytes(candidate)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status", is("Bad Request")))
				.andExpect(jsonPath("$.code", is(400)))
				.andExpect(jsonPath("$.message", is("Email format not correct")));
	}

	@Test
	public void updateApplicationStatus_whenEmailValid_thenOk() throws Exception {

		String offerTitle = "Java-Backend";
		String email = "paul@aol.com";

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(email);
		candidate.setStatus(Status.HIRED);

		mvc.perform(put("/heavenHR/v1/offers/" + offerTitle + "/applications/update")
				.content(mapper.writeValueAsBytes(candidate)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void updateApplicatioStatus_whenOfferInValid_thenBadRequest() throws Exception {

		String offerTitle = "Java-Backend";
		String email = "paul@aol.com";

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(email);
		candidate.setStatus(Status.HIRED);

		doThrow(new InvalidOfferException("Invalid offer title " + offerTitle)).when(this.applicationService)
				.updateApplicationStatus(offerTitle, email, Status.HIRED);

		mvc.perform(put("/heavenHR/v1/offers/" + offerTitle + "/applications/update")
				.content(mapper.writeValueAsBytes(candidate)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status", is("Bad Request")))
				.andExpect(jsonPath("$.code", is(400)))
				.andExpect(jsonPath("$.message", is("Invalid offer title " + offerTitle)));
	}

	@Test
	public void updateApplicatioStatus_whenEmailValidButNotRegistered_thenBadRequest() throws Exception {

		String offerTitle = "Java-Backend";
		String email = "paul@aol.com";

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(email);
		candidate.setStatus(Status.HIRED);

		doThrow(new InvalidApplicantInfoException("Invalid email for applicant " + email)).when(this.applicationService)
				.updateApplicationStatus(offerTitle, email, Status.HIRED);

		mvc.perform(put("/heavenHR/v1/offers/" + offerTitle + "/applications/update")
				.content(mapper.writeValueAsBytes(candidate)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status", is("Bad Request")))
				.andExpect(jsonPath("$.code", is(400)))
				.andExpect(jsonPath("$.message", is("Invalid email for applicant " + email)));
	}

	@Test
	public void updateApplicatioStatus_whenEmailBlank_thenBadRequest() throws Exception {

		String offerTitle = "Java-Backend";
		String email = "";

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(email);
		candidate.setStatus(Status.HIRED);

		mvc.perform(put("/heavenHR/v1/offers/" + offerTitle + "/applications/update")
				.content(mapper.writeValueAsBytes(candidate)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status", is("Bad Request")))
				.andExpect(jsonPath("$.code", is(400))).andExpect(jsonPath("$.message", is("Email must mot be blank")));
	}

	@Test
	public void updateApplicationStatus_whenEmailInValid_thenBadRequest() throws Exception {

		String offerTitle = "Java-Backend";
		String email = "abc123";

		CandidateDto candidate = new CandidateDto();
		candidate.setEmail(email);
		candidate.setStatus(Status.HIRED);

		mvc.perform(put("/heavenHR/v1/offers/" + offerTitle + "/applications/update")
				.content(mapper.writeValueAsBytes(candidate)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status", is("Bad Request")))
				.andExpect(jsonPath("$.code", is(400)))
				.andExpect(jsonPath("$.message", is("Email format not correct")));
	}

}
