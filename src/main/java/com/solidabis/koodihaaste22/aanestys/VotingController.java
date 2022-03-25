package com.solidabis.koodihaaste22.aanestys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VotingController {
    @Autowired
    private VoteRepository voteRepository;

    @PostMapping("/aanestys/{restaurantid}")
    public void registerVote(@PathVariable("restaurantid") String restaurantId) {
        voteRepository.registerVote(restaurantId);
    }
}
