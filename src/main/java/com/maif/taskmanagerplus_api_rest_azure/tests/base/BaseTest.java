package com.maif.taskmanagerplus_api_rest_azure.tests.base;

import org.junit.jupiter.api.BeforeEach;

import com.maif.taskmanagerplus_api_rest_azure.auth.AuthUtil;

import io.restassured.RestAssured;

public class BaseTest {
	
	
    @BeforeEach
    public void setup() {
        AuthUtil.authenticate();
        
        // Sets the complete base URL
        RestAssured.baseURI = "http://localhost:8080/v1";
    }
    
    
}
