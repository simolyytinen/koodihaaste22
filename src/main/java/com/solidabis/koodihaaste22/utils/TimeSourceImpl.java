package com.solidabis.koodihaaste22.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

@Component
public class TimeSourceImpl implements TimeSource {
    private LocalDate fixedDate;

    @Override
    public LocalDate today() {
        return Objects.requireNonNullElseGet(fixedDate, LocalDate::now);
    }

    @Override
    public void stopAt(LocalDate fixedDate) {
        this.fixedDate = fixedDate;
    }
}
