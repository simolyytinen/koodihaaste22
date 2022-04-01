package com.solidabis.koodihaaste22;

import com.solidabis.koodihaaste22.persistence.VoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class VoteRepositoryTests {
    @Autowired
    private VoteRepository repository;

    private final LocalDate TODAY = LocalDate.of(2022,3,30);

    @Test
    @Transactional
    public void shouldSaveAndReturnVoteToday() {
        repository.registerVote("restaurantid", "voterid", TODAY);
        assertEquals(1, repository.getVotes("restaurantid", TODAY));
    }

    @Test
    @Transactional
    public void shouldSaveAndNotReturnForNextDay() {
        repository.registerVote("restaurantid", "voterid", TODAY);
        assertEquals(0, repository.getVotes("restaurantid", TODAY.plusDays(1)));
    }

    @Test
    @Transactional
    public void shouldNotHaveVotesByDefault() {
        assertEquals(0, repository.getVotes("restaurantidnotvoted", TODAY));
    }

    @Test
    @Transactional
    public void shouldReturnMultipleVotesCountCorrentcly() {
        repository.registerVote("restaurantid", "voterid1", TODAY);
        repository.registerVote("restaurantid", "voterid2", TODAY);
        repository.registerVote("restaurantid2", "voterid3", TODAY);
        assertEquals(2, repository.getVotes("restaurantid", TODAY));
    }

    @Test
    @Transactional
    public void shouldReturnAlreadyVotedRestaurantForToday() {
        repository.registerVote("restaurantid", "voterid1", TODAY);
        assertEquals("restaurantid", repository.todaysVote("voterid1", TODAY));
        assertNull(repository.todaysVote("voterid2", TODAY));
    }

    @Test
    @Transactional
    public void shouldReturnVoteResultsForDay() {
        repository.registerVote("restaurant3", "voter6", TODAY);
        repository.registerVote("restaurant1", "voter1", TODAY);
        repository.registerVote("restaurant1", "voter3", TODAY);
        repository.registerVote("restaurant2", "voter4", TODAY);
        repository.registerVote("restaurant1", "voter2", TODAY);
        repository.registerVote("restaurant2", "voter5", TODAY);
        var results = repository.getDayResults(TODAY);
        assertEquals(3, results.size());
        assertEquals(3, results.get(0).getVotes());
        assertEquals(2, results.get(1).getVotes());
        assertEquals(1, results.get(2).getVotes());
    }
}
