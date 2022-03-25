package com.solidabis.koodihaaste22;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DishDTO {
    private String name;
    private String price;
    private List<String> attributes;
}
