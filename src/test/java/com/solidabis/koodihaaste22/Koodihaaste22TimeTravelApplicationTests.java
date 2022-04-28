package com.solidabis.koodihaaste22;

import com.solidabis.koodihaaste22.restaurants.RestaurantSource;
import com.solidabis.koodihaaste22.utils.TimeSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
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

    @MockBean
    private RestaurantSource source;

    @BeforeEach
    public void setup() throws Exception {
        given(source.loadCity("kempele")).willReturn(TestDataUtils.getTestHtml("kempele.html"));
        // restaurants must be loaded first
        mockMvc.perform(loadRestaurants("voterid"));
    }

    @Test
    @DirtiesContext
    public void shouldAllowVoteForARestaurantOnSeparateDays() throws Exception {
        final String restaurant = "30b6b2d95d40d87468c357369e1fe782b17f48092a21520f5d117162a170a50a";

        timeSource.stopAt(LocalDate.of(2022,3,30));

        // given a restaurant has already been voted at previous day
        mockMvc.perform(vote(restaurant, "voterid"))
                .andExpect(status().isOk());
        mockMvc.perform(vote(restaurant, "voterid2"))
                .andExpect(status().isOk());

        mockMvc.perform(loadRestaurants("voterid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurants[0].votes").value(2));

        // when vote another restaurant on another day
        timeSource.stopAt(LocalDate.of(2022,4,1));

        mockMvc.perform(vote(restaurant, "voterid"))
                .andExpect(status().isOk());

        // expect previous days vote to be separate
        mockMvc.perform(loadRestaurants("voterid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurants[0].votes").value(1));
    }

    @Test
    public void shouldReturnHistoricalVotingDataByDate() throws Exception {
        final String restaurant = "30b6b2d95d40d87468c357369e1fe782b17f48092a21520f5d117162a170a50a";
        final String restaurant2 = "5ab414c39694dfd25cf39b684ff0b2d770f48b110231a8d6b6107ad3c34a7f38";

        timeSource.stopAt(LocalDate.of(2022,3,29));

        // given a restaurant has been voted at previous day
        mockMvc.perform(vote(restaurant, "voterid"))
                .andExpect(status().isOk());
        mockMvc.perform(vote(restaurant, "voterid2"))
                .andExpect(status().isOk());

        // and voted another restaurants on another day
        timeSource.stopAt(LocalDate.of(2022,3,31));

        mockMvc.perform(vote(restaurant2, "voterid"))
                .andExpect(status().isOk());

        // expect historical data to be available
        mockMvc.perform(loadResultsByDate("2022-03-29"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").isString())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results", hasSize(1)))
                .andExpect(jsonPath("$.results[0].votes").value(2))
                .andExpect(jsonPath("$.results[0].restaurantid").value(restaurant))
                .andExpect(jsonPath("$.results[0].name").value("Rosso Zeppelin, Kempele"))
                .andExpect(jsonPath("$.results[0].city").value("Kempele"));
    }
}
