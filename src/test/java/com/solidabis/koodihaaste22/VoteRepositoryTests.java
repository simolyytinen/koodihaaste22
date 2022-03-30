package com.solidabis.koodihaaste22;

import com.solidabis.koodihaaste22.persistence.VoteRepository;
import com.solidabis.koodihaaste22.persistence.VoteRepositoryDbImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class VoteRepositoryTests {
    @Autowired
    @Qualifier("dbimpl")
    private VoteRepository repository;

    private final LocalDate TODAY = LocalDate.of(2022,3,30);

    @Test
    @Transactional
    public void shouldSaveAndReturnVoteToday() {
        repository.registerVote("restaurantid", "voterid", TODAY);
        assertEquals(1, repository.getVotes("restaurantid", TODAY));
    }
}
