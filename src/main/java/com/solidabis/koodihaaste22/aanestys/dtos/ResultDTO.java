package com.solidabis.koodihaaste22.aanestys.dtos;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResultDTO {
    private int votes;
    private String restaurantid;
    private String name;
    private String city;
}
