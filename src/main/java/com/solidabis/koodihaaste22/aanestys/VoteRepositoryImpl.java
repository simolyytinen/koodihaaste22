package com.solidabis.koodihaaste22.aanestys;

import com.solidabis.koodihaaste22.persistence.VoteRepository;
import org.javatuples.Pair;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class VoteRepositoryImpl implements VoteRepository {
    private final Map<Pair<LocalDate,String>, Integer> votes = new HashMap<>();
    private final Map<Pair<LocalDate,String>, String> alreadyVoted = new HashMap<>();

    public VoteRepositoryImpl() {
        votes.put(new Pair<>(LocalDate.of(2022,3,30), "9rewu9rewrew9u"), 10);
        votes.put(new Pair<>(LocalDate.of(2022,3,30), "feoij23oij3233"), 0);
        votes.put(new Pair<>(LocalDate.of(2022,4,1), "9rewu9rewrew9u"), 10);
        votes.put(new Pair<>(LocalDate.of(2022,4,1), "feoij23oij3233"), 0);
    }

    public void registerVote(String restaurantId, String voterIdCookie, LocalDate today) {
        var voterId = new Pair<>(today, voterIdCookie);
        if(alreadyVoted.containsKey(voterId)) {
            var votedRestaurant = alreadyVoted.get(voterId);
            votes.compute(new Pair<>(today,votedRestaurant), (key,value) -> value-1);
            alreadyVoted.remove(voterId);
            if(votedRestaurant.equals(restaurantId)) {
                return;
            }
        }
        votes.compute(new Pair<>(today,restaurantId), (key,value) -> value+1);
        alreadyVoted.put(voterId, restaurantId);
    }

    public Integer getVotes(String restaurantId, LocalDate today) {
        return votes.get(new Pair<>(today, restaurantId));
    }
}
