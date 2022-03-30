package com.solidabis.koodihaaste22.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TimeSourceImpl implements TimeSource {
    @Override
    public LocalDate today() {
        return LocalDate.now();
    }
}
