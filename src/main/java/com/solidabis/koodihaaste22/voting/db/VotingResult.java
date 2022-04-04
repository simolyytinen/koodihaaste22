package com.solidabis.koodihaaste22.voting.db;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VotingResult {
    private int votes;
    private String restaurantId;
    private String name;
    private String city;
}
