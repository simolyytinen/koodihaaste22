package com.solidabis.koodihaaste22.persistence;

public interface VoteRepository {
    void registerVote(String restaurantId);
    Integer getVotes(String restaurantId);
}
