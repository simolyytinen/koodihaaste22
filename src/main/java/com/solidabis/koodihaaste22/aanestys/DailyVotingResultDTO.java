package com.solidabis.koodihaaste22.aanestys;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DailyVotingResultDTO {
    private String date;
    private List<ResultDTO> results;
}
