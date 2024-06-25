package com.maif.taskmanagerplus_api_rest_assured.config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

/**
 * RestAssuredConfig class sets up the base configuration for RestAssured.
 * This class sets the base URI for all REST Assured requests.
 * 
 * Author: Maicon Fang
 * Date: 2024-06-19
 */
public class RestAssuredConfig {

    /**
     * The setup method is annotated with @BeforeAll, meaning it will run once before any of the test methods in the class.
     * This method sets the base URI for RestAssured to "http://localhost:8080/v1".
     */
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080/v1";
    }
}
