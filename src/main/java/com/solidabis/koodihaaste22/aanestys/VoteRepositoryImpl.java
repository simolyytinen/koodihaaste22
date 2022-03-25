package com.solidabis.koodihaaste22.aanestys;

import com.solidabis.koodihaaste22.persistence.VoteRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class VoteRepositoryImpl implements VoteRepository {
    private Map<String, Integer> votes = new HashMap<>();

    public VoteRepositoryImpl() {
        votes.put("9rewu9rewrew9u", 10);
    }

    public void registerVote(String restaurantId) {
        votes.compute(restaurantId, (key,value) -> value+1);
    }

    public Integer getVotes(String restaurantId) {
        return votes.get(restaurantId);
    }
}
