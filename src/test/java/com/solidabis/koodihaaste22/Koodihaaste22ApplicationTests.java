package com.solidabis.koodihaaste22;

import com.solidabis.koodihaaste22.restaurants.RestaurantSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static com.solidabis.koodihaaste22.RequestUtils.*;

@SpringBootTest
@AutoConfigureMockMvc
class Koodihaaste22ApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RestaurantSource source;

	@BeforeEach
	void setup() throws Exception {
		given(source.loadCity("kempele")).willReturn(TestDataUtils.getTestHtml("kempele.html"));
		// restaurants must be loaded first
		mockMvc.perform(loadRestaurants("voterid"));
	}

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
		var cookieVoterId = new Cookie(VOTERID_COOKIE_NAME, "voterid");
		mockMvc.perform(get(GET_LOUNASPAIKAT_ENDPOINT).cookie(cookieVoterId))
				.andExpect(status().isOk())
				.andExpect(cookie().doesNotExist(VOTERID_COOKIE_NAME));
	}

	@Test
	void shouldNotAcceptVoteIfNoCookieSet() throws Exception {
		mockMvc.perform(post("/api/v1/vote/30b6b2d95d40d87468c357369e1fe782b17f48092a21520f5d117162a170a50a"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturnListOfLounaspaikatFromKempele() throws Exception {
		/*
		GET /lounaspaikat/Kempele
		{
		   "alreadyVoted": "98rewu98rweu89rew98",
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
				.andExpect(jsonPath("$.alreadyVoted").hasJsonPath())
				.andExpect(jsonPath("$.date", matchesPattern("^[0-9]{4}-[0-9]{2}-[0-9]{2}$")))
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
		final String restaurantId = "30b6b2d95d40d87468c357369e1fe782b17f48092a21520f5d117162a170a50a";

		mockMvc.perform(vote(restaurantId, "voterid"))
				.andExpect(status().isOk());

		mockMvc.perform(loadRestaurants("voterid"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.alreadyVoted").value(restaurantId))
				.andExpect(jsonPath("$.restaurants[0].votes").value(1));
	}

	@Test
	@DirtiesContext
	public void shouldChangeVoteToDifferentRestaurantIfRevotedTheSameDay() throws Exception {
		final String restaurant1 = "30b6b2d95d40d87468c357369e1fe782b17f48092a21520f5d117162a170a50a";
		final String restaurant2 = "5ab414c39694dfd25cf39b684ff0b2d770f48b110231a8d6b6107ad3c34a7f38";

		// given a restaurant has already been voted
		mockMvc.perform(vote(restaurant1, "voterid"))
				.andExpect(status().isOk());

		mockMvc.perform(loadRestaurants("voterid"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.restaurants[0].votes").value(1))
				.andExpect(jsonPath("$.restaurants[1].votes").value(0));

		// when vote another restaurant
		mockMvc.perform(vote(restaurant2, "voterid"))
				.andExpect(status().isOk());

		// expect original restaurant vote to be removed
		// and the latter restaurant to have a vote
		mockMvc.perform(loadRestaurants("voterid"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.alreadyVoted").value(restaurant2))
				.andExpect(jsonPath("$.restaurants[0].votes").value(0))
				.andExpect(jsonPath("$.restaurants[1].votes").value(1));
	}

	@Test
	public void shouldRemoveVoteIfVotesTheSameRestaurantAgain() throws Exception {
		final String restaurant = "5ab414c39694dfd25cf39b684ff0b2d770f48b110231a8d6b6107ad3c34a7f38";
		// given a restaurant has already been voted
		mockMvc.perform(vote(restaurant, "voterid"))
				.andExpect(status().isOk());
		// when a restaurant is revoted
		mockMvc.perform(vote(restaurant, "voterid"))
				.andExpect(status().isOk());
		// expect vote to be removed
		mockMvc.perform(loadRestaurants("voterid"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.alreadyVoted").doesNotExist())
				.andExpect(jsonPath("$.restaurants[0].votes").value(0));
	}

	@Test
	@DirtiesContext
	public void shouldAcceptVotesForSingleRestaurantFromMultipleVoters() throws Exception {
		final String restaurant = "30b6b2d95d40d87468c357369e1fe782b17f48092a21520f5d117162a170a50a";
		mockMvc.perform(vote(restaurant, "voterid 1"))
				.andExpect(status().isOk());

		mockMvc.perform(vote(restaurant, "voterid 2"))
				.andExpect(status().isOk());

		mockMvc.perform(loadRestaurants("voterid 1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.alreadyVoted").value(restaurant))
				.andExpect(jsonPath("$.restaurants[0].votes").value(2));
	}

	@Test
	public void shouldNotAllowVoteForRestaurantThatHasNotLoadedYet() throws Exception {
		final String restaurant = "30b6b2d95d40d87468c357369e1fe782b17f48092a21520f5d117162a170a50b";
		mockMvc.perform(vote(restaurant, "voterid 1"))
				.andExpect(status().isNotFound());
	}

	@Test
	@DirtiesContext
	public void shouldReturnDayResults() throws Exception {
		final String restaurant1 = "30b6b2d95d40d87468c357369e1fe782b17f48092a21520f5d117162a170a50a";
		final String restaurant2 = "5ab414c39694dfd25cf39b684ff0b2d770f48b110231a8d6b6107ad3c34a7f38";

		// 3 votes for restaurant 1
		mockMvc.perform(vote(restaurant1, "voter1")).andExpect(status().isOk());
		mockMvc.perform(vote(restaurant1, "voter2")).andExpect(status().isOk());
		mockMvc.perform(vote(restaurant1, "voter3")).andExpect(status().isOk());

		// 1 vote fro restaurant 2
		mockMvc.perform(vote(restaurant2, "voter4")).andExpect(status().isOk());

		mockMvc.perform(loadResults())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.date").isString())
				.andExpect(jsonPath("$.results").isArray())
				.andExpect(jsonPath("$.results", hasSize(2)))
				.andExpect(jsonPath("$.results[0].votes").value(3))
				.andExpect(jsonPath("$.results[0].restaurantid").value(restaurant1))
				.andExpect(jsonPath("$.results[0].name").value("Rosso Zeppelin, Kempele"))
				.andExpect(jsonPath("$.results[0].city").value("Kempele"))
				.andExpect(jsonPath("$.results[1].votes").value(1))
				.andExpect(jsonPath("$.results[1].restaurantid").value(restaurant2))
				.andExpect(jsonPath("$.results[1].name").value("Shell HelmiSimpukka Kempele Zeppelin"))
				.andExpect(jsonPath("$.results[1].city").value("Kempele"));
	}
}
