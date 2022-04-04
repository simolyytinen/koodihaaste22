package com.solidabis.koodihaaste22.voting.dtos;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class VotingResultDTO {
    private String date;
    private List<RestaurantVotesDTO> results;
}
