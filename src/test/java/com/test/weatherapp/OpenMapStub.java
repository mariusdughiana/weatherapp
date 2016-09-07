package com.test.weatherapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenMapStub {
    protected static final String OPENMAP_RESPONSE_CITY = "{\"coord\":{\"lon\":-0.13,\"lat\":51.51},\"weather\":[{\"id\":800," +
            "\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"base\":\"cmc " +
            "stations\",\"main\":{\"temp\":298.52,\"pressure\":1015,\"humidity\":39," +
            "\"temp_min\":295.37,\"temp_max\":301.15},\"wind\":{\"speed\":3.1,\"deg\":140}," +
            "\"clouds\":{\"all\":0},\"dt\":1473270118,\"sys\":{\"type\":1,\"id\":5091," +
            "\"message\":0.0129,\"country\":\"GB\",\"sunrise\":1473225862,\"sunset\":1473273051}," +
            "\"id\":2643743,\"name\":\"London\",\"cod\":200}";


    protected static JsonNode openMapResponseAsJsonNode() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(OPENMAP_RESPONSE_CITY);
    }
}
