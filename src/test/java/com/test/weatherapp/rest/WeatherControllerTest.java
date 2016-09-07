package com.test.weatherapp.rest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.test.weatherapp.OpenMapStub;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherControllerTest extends OpenMapStub {

    private static final WireMockServer WIRE_MOCK_SERVER = new WireMockServer(wireMockConfig().port(8089).bindAddress("localhost"));



    @BeforeClass
    public static void startServer() {
        WIRE_MOCK_SERVER.start();
        configureFor("localhost", 8089);
    }

    @AfterClass
    public static void stopServer() {
        WIRE_MOCK_SERVER.stop();
    }

    private MockMvc mockMvc;



    @Autowired
    private WebApplicationContext webApplicationContext;

    @Value("${weatherapp.openweather.appid}")
    private String appid;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();

    }
    @Test
    public void weatherByCityTest() throws Exception {

        givenThat(WireMock.get(urlMatching("/data/2.5/weather.*"))
                          .withQueryParam("q", equalTo("london"))
                          .withQueryParam("APPID", equalTo(appid))
                          .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(OPENMAP_RESPONSE_CITY)));

        mockMvc.perform(get("/weather/city/london")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().string(
                       "{" +
                               "\"date\":\"2016-09-07\"," +
                               "\"tempF\":77,\"sunrise\":" +
                               "\"6:24 AM\"," +
                               "\"city\":\"London\"," +
                               "\"sunset\":\"7:30 PM\"," +
                               "\"description\":\"clear sky\"," +
                               "\"tempC\":25" +
                       "}"));
    }

    @Test
    public void weatherByCityTestWhenOpenMapFails() throws Exception {

        givenThat(WireMock.get(urlMatching("/data/2.5/weather.*"))
                          .withQueryParam("q", equalTo("london"))
                          .withQueryParam("APPID", equalTo(appid))
                          .willReturn(aResponse()
                                  .withStatus(500)
                                  .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"some error\"}")));

        mockMvc.perform(get("/weather/city/london")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isInternalServerError())
               .andExpect(content().string("{\"error\":\"some error\"}"));
    }

}