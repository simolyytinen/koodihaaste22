package com.solidabis.koodihaaste22.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TimeSourceImpl implements TimeSource {
    private LocalDate fixedDate;

    @Override
    public LocalDate today() {
        if(fixedDate!=null) {
            return fixedDate;
        } else {
            return LocalDate.now();
        }
    }

    @Override
    public void stopAt(LocalDate fixedDate) {
        this.fixedDate = fixedDate;
    }
}
