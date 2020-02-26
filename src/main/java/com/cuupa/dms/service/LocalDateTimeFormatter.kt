package com.cuupa.dms.service

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeFormatter {
    fun format(localDateTime: LocalDateTime): String {
        return localDateTime.format(formatter)
    }

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm")
    }
}