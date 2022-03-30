package com.solidabis.koodihaaste22.persistence;

import java.time.LocalDate;

public interface VoteRepository {
    void registerVote(String restaurantId, String voterId, LocalDate today);
    int getVotes(String restaurantId, LocalDate today);
    String todaysVote(String voterId, LocalDate today);
}
