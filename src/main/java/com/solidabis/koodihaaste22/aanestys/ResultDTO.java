package com.solidabis.koodihaaste22.aanestys;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResultDTO {
    private int votes;
    private String restaurantid;
}
