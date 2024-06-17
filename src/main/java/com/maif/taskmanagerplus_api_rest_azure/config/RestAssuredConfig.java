package com.maif.taskmanagerplus_api_rest_azure.config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class RestAssuredConfig {
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080/v1";
    }
}