package com.test.weatherapp.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class OpenWeatherDataParser {

    public Map<String, Object> parseWeatherDataForCity(JsonNode data) {
        Map<String, Object> result = new HashMap<>();

        result.put("date", LocalDate.now().toString());
        result.put("city", data.findValue("name").asText());
        result.put("description", data.findValue("description").asText());
        final double tempK = data.findValue("temp").asDouble();

        Double tempF = tempK* 9/5 - 459.67;
        result.put("tempF", tempF.intValue());
        Double tempC = tempK - 273;
        result.put("tempC", tempC.intValue());

        result.put("sunrise", extractTime(data.findValue("sunrise").asLong()));
        result.put("sunset", extractTime(data.findValue("sunset").asLong()));

        return result;
    }

    private String extractTime(long utcTimeInSeconds) {
        Date localTime = new Date(utcTimeInSeconds*1000);
        String format = "h:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(localTime);
    }
}
