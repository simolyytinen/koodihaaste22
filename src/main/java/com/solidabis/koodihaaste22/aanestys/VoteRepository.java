package com.solidabis.koodihaaste22.aanestys;

import com.solidabis.koodihaaste22.aanestys.db.VotingResult;
import com.solidabis.koodihaaste22.persistence.TodaysVoteRepository;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository extends TodaysVoteRepository {
    void registerVote(String restaurantId, String voterId, LocalDate today);
    List<VotingResult> getDayResults(LocalDate today);
}
