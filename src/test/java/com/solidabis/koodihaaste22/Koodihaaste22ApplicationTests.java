package com.solidabis.koodihaaste22;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
		mockMvc.perform(get("/lounaspaikat/Kempele"))
				.andExpect(status().isOk())
				.andExpect(cookie().exists("VOTERID"))
				.andExpect(cookie().httpOnly("VOTERID", true));
	}
}
