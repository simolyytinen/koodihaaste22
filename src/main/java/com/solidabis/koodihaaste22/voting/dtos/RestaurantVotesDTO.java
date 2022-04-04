package com.solidabis.koodihaaste22.voting.dtos;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RestaurantVotesDTO {
    private int votes;
    private String restaurantid;
    private String name;
    private String city;
}
