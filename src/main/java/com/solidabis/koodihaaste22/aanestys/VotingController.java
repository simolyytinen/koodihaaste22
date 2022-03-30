package com.solidabis.koodihaaste22.aanestys;

import com.solidabis.koodihaaste22.persistence.VoteRepository;
import com.solidabis.koodihaaste22.utils.Constants;
import com.solidabis.koodihaaste22.utils.TimeSource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VotingController {
    private final VoteRepository voteRepository;
    private final TimeSource timeSource;

    public VotingController(VoteRepository voteRepository, TimeSource timeSource) {
        this.voteRepository = voteRepository;
        this.timeSource = timeSource;
    }

    @PostMapping("/aanestys/{restaurantid}")
    @Operation(summary = "Give/remove a vote for given restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vote/vote removal was succesful"),
            @ApiResponse(responseCode = "400", description = "VOTERID cookie was missing"),
            @ApiResponse(responseCode = "500", description = "Database error occurred")
    })
    @Transactional
    public void registerVote(@PathVariable("restaurantid") String restaurantId,
                             @CookieValue(name = Constants.VOTERID_COOKIE_NAME) String voterIdCookie) {
        voteRepository.registerVote(restaurantId, voterIdCookie, timeSource.today());
    }
}
