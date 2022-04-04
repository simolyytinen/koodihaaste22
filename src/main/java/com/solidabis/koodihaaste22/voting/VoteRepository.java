package com.solidabis.koodihaaste22.voting;

import com.solidabis.koodihaaste22.voting.db.VotingResult;
import com.solidabis.koodihaaste22.persistence.TodaysVoteRepository;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository extends TodaysVoteRepository {
    void registerVote(String restaurantId, String voterId, LocalDate today);
    List<VotingResult> getResults(LocalDate today);
}
