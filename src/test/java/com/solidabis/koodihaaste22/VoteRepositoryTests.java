package com.solidabis.koodihaaste22;

import com.solidabis.koodihaaste22.voting.VoteRepository;
import com.solidabis.koodihaaste22.restaurants.db.RestaurantRepository;
import com.solidabis.koodihaaste22.restaurants.parsing.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class VoteRepositoryTests {
    @Autowired
    private VoteRepository repository;

    @Autowired
    private RestaurantRepository restaurantRepository;


    private final LocalDate TODAY = LocalDate.of(2022,3,30);

    private final Restaurant restaurant1 = Restaurant.builder()
            .name("restaurant1")
            .city("Kempele")
            .build();
    private final Restaurant restaurant2 = Restaurant.builder()
            .name("restaurant2")
            .city("Oulu")
            .build();

    @BeforeEach
    public void setup() {
        restaurantRepository.saveRestaurant(restaurant1);
        restaurantRepository.saveRestaurant(restaurant2);
    }

    @Test
    @Transactional
    public void shouldSaveAndReturnVoteToday() {
        repository.registerVote(restaurant1.id(), "voterid", TODAY);
        assertEquals(1, repository.getVotes(restaurant1.id(), TODAY));
    }

    @Test
    @Transactional
    public void shouldSaveAndNotReturnForNextDay() {
        repository.registerVote(restaurant1.id(), "voterid", TODAY);
        assertEquals(0, repository.getVotes(restaurant1.id(), TODAY.plusDays(1)));
    }

    @Test
    @Transactional
    public void shouldNotHaveVotesByDefault() {
        assertEquals(0, repository.getVotes("restaurantidnotvoted", TODAY));
    }

    @Test
    @Transactional
    public void shouldReturnMultipleVotesCountCorrentcly() {
        repository.registerVote(restaurant1.id(), "voterid1", TODAY);
        repository.registerVote(restaurant1.id(), "voterid2", TODAY);
        repository.registerVote(restaurant2.id(), "voterid3", TODAY);
        assertEquals(2, repository.getVotes(restaurant1.id(), TODAY));
    }

    @Test
    @Transactional
    public void shouldReturnAlreadyVotedRestaurantForToday() {
        repository.registerVote(restaurant1.id(), "voterid1", TODAY);
        assertEquals(restaurant1.id(), repository.todaysVote("voterid1", TODAY));
        assertNull(repository.todaysVote("voterid2", TODAY));
    }

    @Test
    @Transactional
    public void shouldReturnVoteResultsForDay() {
        repository.registerVote(restaurant1.id(), "voter1", TODAY);
        repository.registerVote(restaurant1.id(), "voter3", TODAY);
        repository.registerVote(restaurant2.id(), "voter4", TODAY);
        repository.registerVote(restaurant1.id(), "voter2", TODAY);
        repository.registerVote(restaurant2.id(), "voter5", TODAY);
        var results = repository.getResults(TODAY);
        assertEquals(2, results.size());
        assertEquals(3, results.get(0).getVotes());
        assertEquals("restaurant1", results.get(0).getName());
        assertEquals("Kempele", results.get(0).getCity());
        assertEquals(2, results.get(1).getVotes());
        assertEquals("restaurant2", results.get(1).getName());
        assertEquals("Oulu", results.get(1).getCity());
    }
}
