package br.com.marcelo.azevedo.util;

import java.time.LocalDateTime;

import static br.com.marcelo.azevedo.util.Constants.FORMATTER_YYYY_MM_DD;

public class StringConverter {

    public static String getShortStringDate(LocalDateTime dateTimeToConvert) {
        return FORMATTER_YYYY_MM_DD.format(dateTimeToConvert);
    }

}
