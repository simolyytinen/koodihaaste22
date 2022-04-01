package com.solidabis.koodihaaste22.persistence;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VotingResult {
    private int votes;
    private String restaurantId;
}
