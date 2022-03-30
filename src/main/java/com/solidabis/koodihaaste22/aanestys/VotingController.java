package com.solidabis.koodihaaste22.aanestys;

import com.solidabis.koodihaaste22.persistence.VoteRepository;
import com.solidabis.koodihaaste22.utils.Constants;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VotingController {
    private final VoteRepository voteRepository;

    public VotingController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @PostMapping("/aanestys/{restaurantid}")
    public void registerVote(@PathVariable("restaurantid") String restaurantId,
                             @CookieValue(name= Constants.VOTERID_COOKIE_NAME) String voterIdCookie) {
        voteRepository.registerVote(restaurantId, voterIdCookie);
    }
}
