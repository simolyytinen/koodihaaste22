package com.solidabis.koodihaaste22.aanestys;

import com.solidabis.koodihaaste22.persistence.VoteRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class VoteRepositoryImpl implements VoteRepository {
    private final Map<String, Integer> votes = new HashMap<>();
    private final Map<String, String> alreadyVoted = new HashMap<>();

    public VoteRepositoryImpl() {
        votes.put("9rewu9rewrew9u", 10);
        votes.put("feoij23oij3233", 0);
    }

    public void registerVote(String restaurantId, String voterIdCookie) {
        if(alreadyVoted.containsKey(voterIdCookie)) {
            var votedRestaurant = alreadyVoted.get(voterIdCookie);
            votes.compute(votedRestaurant, (key,value) -> value-1);
        }
        votes.compute(restaurantId, (key,value) -> value+1);
        alreadyVoted.put(voterIdCookie, restaurantId);
    }

    public Integer getVotes(String restaurantId) {
        return votes.get(restaurantId);
    }
}
