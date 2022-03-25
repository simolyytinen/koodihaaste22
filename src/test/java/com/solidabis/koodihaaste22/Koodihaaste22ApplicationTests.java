package com.solidabis.koodihaaste22;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class Koodihaaste22ApplicationTests {
	public static final String VOTERID_COOKIE_NAME = "VOTERID";
	public static final String GET_LOUNASPAIKAT_ENDPOINT = "/lounaspaikat/Kempele";

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
}
