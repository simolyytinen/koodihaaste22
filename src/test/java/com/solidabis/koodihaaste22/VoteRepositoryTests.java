package com.solidabis.koodihaaste22;

import com.solidabis.koodihaaste22.aanestys.VoteRepository;
import com.solidabis.koodihaaste22.lounaspaikat.db.LounaspaikkaRepository;
import com.solidabis.koodihaaste22.lounaspaikat.parsing.LounasPaikka;
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
    private LounaspaikkaRepository lounaspaikkaRepository;


    private final LocalDate TODAY = LocalDate.of(2022,3,30);

    private final LounasPaikka paikka1 = LounasPaikka.builder()
            .name("restaurant1")
            .city("Kempele")
            .build();
    private final LounasPaikka paikka2 = LounasPaikka.builder()
            .name("restaurant2")
            .city("Kempele")
            .build();

    @BeforeEach
    public void setup() {
        lounaspaikkaRepository.saveRestaurant(paikka1);
        lounaspaikkaRepository.saveRestaurant(paikka2);
    }

    @Test
    @Transactional
    public void shouldSaveAndReturnVoteToday() {
        repository.registerVote(paikka1.id(), "voterid", TODAY);
        assertEquals(1, repository.getVotes(paikka1.id(), TODAY));
    }

    @Test
    @Transactional
    public void shouldSaveAndNotReturnForNextDay() {
        repository.registerVote(paikka1.id(), "voterid", TODAY);
        assertEquals(0, repository.getVotes(paikka1.id(), TODAY.plusDays(1)));
    }

    @Test
    @Transactional
    public void shouldNotHaveVotesByDefault() {
        assertEquals(0, repository.getVotes("restaurantidnotvoted", TODAY));
    }

    @Test
    @Transactional
    public void shouldReturnMultipleVotesCountCorrentcly() {
        repository.registerVote(paikka1.id(), "voterid1", TODAY);
        repository.registerVote(paikka1.id(), "voterid2", TODAY);
        repository.registerVote(paikka2.id(), "voterid3", TODAY);
        assertEquals(2, repository.getVotes(paikka1.id(), TODAY));
    }

    @Test
    @Transactional
    public void shouldReturnAlreadyVotedRestaurantForToday() {
        repository.registerVote(paikka1.id(), "voterid1", TODAY);
        assertEquals(paikka1.id(), repository.todaysVote("voterid1", TODAY));
        assertNull(repository.todaysVote("voterid2", TODAY));
    }

    @Test
    @Transactional
    public void shouldReturnVoteResultsForDay() {
        repository.registerVote(paikka1.id(), "voter1", TODAY);
        repository.registerVote(paikka1.id(), "voter3", TODAY);
        repository.registerVote(paikka2.id(), "voter4", TODAY);
        repository.registerVote(paikka1.id(), "voter2", TODAY);
        repository.registerVote(paikka2.id(), "voter5", TODAY);
        var results = repository.getDayResults(TODAY);
        assertEquals(2, results.size());
        assertEquals(3, results.get(0).getVotes());
        assertEquals(2, results.get(1).getVotes());
    }
}
