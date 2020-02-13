package com.cuupa.dms.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeFormatter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm");

    public String format(LocalDateTime localDateTime) {
        return localDateTime.format(formatter);
    }
}
