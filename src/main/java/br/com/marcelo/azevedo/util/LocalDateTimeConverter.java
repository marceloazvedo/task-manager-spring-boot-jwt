package br.com.marcelo.azevedo.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static br.com.marcelo.azevedo.util.Constants.FORMATTER_YYYY_MM_DD;

public class LocalDateTimeConverter {

    public static LocalDateTime getByShortStringFormat(String dateInShortFormmat) {
        return LocalDate.parse(dateInShortFormmat, FORMATTER_YYYY_MM_DD).atStartOfDay();
    }

}
