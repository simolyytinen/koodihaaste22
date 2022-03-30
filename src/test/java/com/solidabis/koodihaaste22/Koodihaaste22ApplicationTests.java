package com.solidabis.koodihaaste22;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static com.solidabis.koodihaaste22.TestConstants.GET_LOUNASPAIKAT_ENDPOINT;
import static com.solidabis.koodihaaste22.TestConstants.VOTERID_COOKIE_NAME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class Koodihaaste22ApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldSetVoterIdIfNoCookieSet() throws Exception {
		// GET /lounaspaikat/Kempele
		// requestissa ei cookieta:
		// niin responsessa tulee http-only cookie
		mockMvc.perform(get(GET_LOUNASPAIKAT_ENDPOINT))
				.andExpect(status().isOk())
				.andExpect(cookie().exists(VOTERID_COOKIE_NAME))
				.andExpect(cookie().httpOnly(VOTERID_COOKIE_NAME, true));
	}

	@Test
	void shouldNotSetVoterIdIfCookieSet() throws Exception {
		var cookieVoterId = new Cookie(VOTERID_COOKIE_NAME, "Höttöä");
		mockMvc.perform(get(GET_LOUNASPAIKAT_ENDPOINT).cookie(cookieVoterId))
				.andExpect(status().isOk())
				.andExpect(cookie().doesNotExist(VOTERID_COOKIE_NAME));
	}

	@Test
	void shouldNotAcceptVoteIfNoCookieSet() throws Exception {
		mockMvc.perform(post("/aanestys/9rewu9rewrew9u"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturnListOfLounaspaikatFromKempele() throws Exception {
		/*
		GET /lounaspaikat/Kempele
		{
		   "alreadyVoted": false,
		   "restaurants": [
		   	   {
		   	      "id": "98rewu98rweu89rew98",
		   	      "name": "Shell HelmiSimpukka Kempele",
		   	      "openingHours": "10-14",
		   	      "votes": 10,
		   	      "dishes": [
		   	         { "name": "Helmen lihapullaa ja ruskeaa kastiketta",
		   	           "price": "10,90EUR",
		   	           "attributes": ["G","L"]
		   	         }
		   	      ]
		   	   }
		   ]
		}
		 */
		mockMvc.perform(get(GET_LOUNASPAIKAT_ENDPOINT))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.alreadyVoted").isBoolean())
				.andExpect(jsonPath("$.restaurants").isArray())
				.andExpect(jsonPath("$.restaurants[0].id").isString())
				.andExpect(jsonPath("$.restaurants[0].name").isString())
				.andExpect(jsonPath("$.restaurants[0].openingHours").isString())
				.andExpect(jsonPath("$.restaurants[0].votes").isNumber())
				.andExpect(jsonPath("$.restaurants[0].dishes").isArray())
				.andExpect(jsonPath("$.restaurants[0].dishes[0].name").isString())
				.andExpect(jsonPath("$.restaurants[0].dishes[0].price").isString())
				.andExpect(jsonPath("$.restaurants[0].dishes[0].attributes").isArray());
	}

	@Test
	@DirtiesContext
	public void shouldAddVoteCountAfterVoting() throws Exception {
		var cookieVoterId = new Cookie(VOTERID_COOKIE_NAME, "Höttöä");

		mockMvc.perform(post("/aanestys/9rewu9rewrew9u").cookie(cookieVoterId))
				.andExpect(status().isOk());

		mockMvc.perform(get(GET_LOUNASPAIKAT_ENDPOINT))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.restaurants[0].votes").value(11));
	}

	@Test
	@DirtiesContext
	public void shouldChangeVoteToDifferentRestaurantIfRevotedTheSameDay() throws Exception {
		var cookieVoterId = new Cookie(VOTERID_COOKIE_NAME, "Höttöä");

		// given a restaurant has already been voted
		mockMvc.perform(post("/aanestys/9rewu9rewrew9u").cookie(cookieVoterId))
				.andExpect(status().isOk());

		mockMvc.perform(get(GET_LOUNASPAIKAT_ENDPOINT))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.restaurants[0].votes").value(11))
				.andExpect(jsonPath("$.restaurants[1].votes").value(0));

		// when vote another restaurant
		mockMvc.perform(post("/aanestys/feoij23oij3233").cookie(cookieVoterId))
				.andExpect(status().isOk());

		// expect original restaurant vote to be removed
		// and the latter restaurant to have a vote
		mockMvc.perform(get(GET_LOUNASPAIKAT_ENDPOINT))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.restaurants[0].votes").value(10))
				.andExpect(jsonPath("$.restaurants[1].votes").value(1));
	}

	@Test
	public void shouldRemoveVoteIfVotesTheSameRestaurantAgain() throws Exception {
		var cookieVoterId = new Cookie(VOTERID_COOKIE_NAME, "Höttöä");

		// given a restaurant has already been voted
		mockMvc.perform(post("/aanestys/9rewu9rewrew9u").cookie(cookieVoterId))
				.andExpect(status().isOk());
		// when a restaurant is revoted
		mockMvc.perform(post("/aanestys/9rewu9rewrew9u").cookie(cookieVoterId))
				.andExpect(status().isOk());

		// expect vote to be removed
		mockMvc.perform(get(GET_LOUNASPAIKAT_ENDPOINT))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.restaurants[0].votes").value(10));
	}

	@Test
	@DirtiesContext
	public void shouldAcceptVotesForSingleRestaurantFromMultipleVoters() throws Exception {
		var cookieVoterId = new Cookie(VOTERID_COOKIE_NAME, "Höttöä 1");
		mockMvc.perform(post("/aanestys/9rewu9rewrew9u").cookie(cookieVoterId))
				.andExpect(status().isOk());

		var cookieVoterId2 = new Cookie(VOTERID_COOKIE_NAME, "Höttöä 2");
		mockMvc.perform(post("/aanestys/9rewu9rewrew9u").cookie(cookieVoterId2))
				.andExpect(status().isOk());

		mockMvc.perform(get(GET_LOUNASPAIKAT_ENDPOINT))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.restaurants[0].votes").value(12));
	}
}
