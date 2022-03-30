package com.solidabis.koodihaaste22.lounaspaikat;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Dish {
    private String name;
    private List<String> attributes;
    private String price;
}
