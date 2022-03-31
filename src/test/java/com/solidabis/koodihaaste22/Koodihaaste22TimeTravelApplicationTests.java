package com.solidabis.koodihaaste22;

import com.solidabis.koodihaaste22.utils.TimeSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.solidabis.koodihaaste22.RequestUtils.*;

@SpringBootTest
@AutoConfigureMockMvc
public class Koodihaaste22TimeTravelApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TimeSource timeSource;

    @Test
    @DirtiesContext
    public void shouldAllowVoteForARestaurantOnSeparateDays() throws Exception {
        final String restaurant = "30b6b2d95d40d87468c357369e1fe782b17f48092a21520f5d117162a170a50a";

        timeSource.stopAt(LocalDate.of(2022,3,30));

        // given a restaurant has already been voted at previous day
        mockMvc.perform(vote(restaurant, "voterid"))
                .andExpect(status().isOk());

        mockMvc.perform(loadRestaurants("voterid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurants[0].votes").value(1));

        // when vote another restaurant on another day
        timeSource.stopAt(LocalDate.of(2022,4,1));

        mockMvc.perform(vote(restaurant, "voterid"))
                .andExpect(status().isOk());

        // expect previous days vote to be separate
        mockMvc.perform(loadRestaurants("voterid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurants[0].votes").value(1));
    }
}
