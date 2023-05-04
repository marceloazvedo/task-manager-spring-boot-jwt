package br.com.marcelo.azevedo.util;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDynamoDBConverter implements DynamoDBTypeConverter<String, LocalDateTime> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String convert(LocalDateTime localDateTimeToConvert) {
        return formatter.format(localDateTimeToConvert);
    }

    @Override
    public LocalDateTime unconvert(String localDateTimeInString) {
        return LocalDateTime.parse(localDateTimeInString, formatter);
    }
}
