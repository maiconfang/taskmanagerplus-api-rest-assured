package com.maif.taskmanagerplus_api_rest_assured.tests.base;

import org.junit.jupiter.api.BeforeAll;

import com.maif.taskmanagerplus_api_rest_assured.auth.AuthUtil;

import io.restassured.RestAssured;

/**
 * BaseTest class sets up the necessary configuration for running tests.
 * This class performs authentication and sets the base URI for REST Assured.
 * 
 * Author: Maicon Fang
 * Date: 2024-06-19
 * 
 */
public class BaseTest {

    /**
     * The setup method is annotated with @BeforeAll, meaning it will run once before any of the test methods in the class.
     * This method performs the following actions:
     * 1. Authenticates the user using AuthUtil.
     * 2. Sets the base URI for REST Assured to "http://localhost:8080/v1".
     */
    @BeforeAll
    public static void setup() {
        
        // Perform authentication
        AuthUtil.authenticate();
        
        // Set the complete base URL
        RestAssured.baseURI = "http://localhost:8080/v1";
    }
    
}
