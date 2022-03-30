package com.solidabis.koodihaaste22.persistence;

import java.time.LocalDate;

public interface VoteRepository {
    void registerVote(String restaurantId, String voterIdCookie, LocalDate today);
    int getVotes(String restaurantId, LocalDate today);
}
