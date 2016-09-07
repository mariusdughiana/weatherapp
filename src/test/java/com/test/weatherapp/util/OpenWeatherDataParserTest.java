package com.test.weatherapp.util;

import com.test.weatherapp.OpenMapStub;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Map;

public class OpenWeatherDataParserTest extends OpenMapStub {

    private OpenWeatherDataParser openWeatherDataParser = new OpenWeatherDataParser();

    @Test
    public void parseWeatherDataForCity() throws Exception {
        Map<String, Object> response = openWeatherDataParser.parseWeatherDataForCity(openMapResponseAsJsonNode());
        Assert.assertEquals(7, response.size());
        Assert.assertEquals(LocalDate.now().toString(), response.get("date"));
        Assert.assertEquals("London", response.get("city"));
        Assert.assertEquals("clear sky", response.get("description"));
        Assert.assertEquals("77", response.get("tempF").toString());
        Assert.assertEquals("25", response.get("tempC").toString());
        Assert.assertEquals("6:24 AM", response.get("sunrise").toString());
        Assert.assertEquals("7:30 PM", response.get("sunset").toString());
    }
}