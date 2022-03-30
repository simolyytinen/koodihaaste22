package com.solidabis.koodihaaste22.persistence;

public interface VoteRepository {
    void registerVote(String restaurantId, String voterIdCookie);
    Integer getVotes(String restaurantId);
}
