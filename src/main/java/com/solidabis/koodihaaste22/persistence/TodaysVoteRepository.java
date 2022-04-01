package com.solidabis.koodihaaste22.persistence;

import java.time.LocalDate;

public interface TodaysVoteRepository {
    String todaysVote(String voterId, LocalDate today);
    int getVotes(String restaurantId, LocalDate today);
}
