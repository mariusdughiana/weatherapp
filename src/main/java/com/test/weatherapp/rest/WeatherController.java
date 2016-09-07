package com.test.weatherapp.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.test.weatherapp.util.OpenWeatherDataParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private RestTemplate restTemplate;


    @Value("${weatherapp.openweather.baseUrl}")
    private String baseUrl;

    @Value("${weatherapp.openweather.appid}")
    private String appid;

    @Autowired
    private OpenWeatherDataParser openWeatherDataParser;

    @RequestMapping(
            value = "/city/{cityName}",
            method = RequestMethod.GET,
            produces = "application/json")
    public Object city(@PathVariable String cityName) {

        try {

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                                                               .queryParam("q", cityName)
                                                               .queryParam("APPID", appid);
            JsonNode openWeatherData = restTemplate.getForEntity(builder.build().toUriString(), JsonNode.class).getBody();
            return new ResponseEntity<Map>(openWeatherDataParser.parseWeatherDataForCity(openWeatherData), HttpStatus.OK);
        } catch (HttpServerErrorException ex) {
            return new ResponseEntity<>(ex.getResponseBodyAsString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
