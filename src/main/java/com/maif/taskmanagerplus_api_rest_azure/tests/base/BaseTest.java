package com.maif.taskmanagerplus_api_rest_azure.tests.base;

import org.junit.jupiter.api.BeforeAll;

import com.maif.taskmanagerplus_api_rest_azure.auth.AuthUtil;

import io.restassured.RestAssured;

public class BaseTest {
	
	@BeforeAll
	public static void setup() {
		
        // Perform authentication
        AuthUtil.authenticate();
        
        // Set the complete base URL
        RestAssured.baseURI = "http://localhost:8080/v1";
    }
    
    
}
