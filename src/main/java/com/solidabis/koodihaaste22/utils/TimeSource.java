package com.solidabis.koodihaaste22.utils;

import java.time.LocalDate;

public interface TimeSource {
    LocalDate today();
    void stopAt(LocalDate fixedDate);
}
