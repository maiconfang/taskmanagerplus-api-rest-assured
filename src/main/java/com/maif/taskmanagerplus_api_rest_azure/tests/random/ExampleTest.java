package com.maif.taskmanagerplus_api_rest_azure.tests.random;


import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import com.maif.taskmanagerplus_api_rest_azure.tests.base.BaseTest;
import com.maif.taskmanagerplus_api_rest_azure.tests.util.TestUtil;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


/**
 * ExampleTest class contains test methods for the Task API endpoints.
 * This class extends BaseTest to inherit the common setup configuration.
 * 
 * Author: Maicon Fang
 * Date: 2024-06-19
 */

public class ExampleTest extends BaseTest {
	
    private static final String TASKS_PATH = "/tasks"; // Static variable for the path "/tasks"

    @Test
    public void testGetUserEndpoint() {
        given()
            .when()
            .get("/users/{userId}", 1)
            .then()
            .statusCode(200)
            .body("id", equalTo(1));
    }
    
    
    @Test
    public void testFilterTasks() {
        given()
            .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
            .queryParam("dueDate", "2024-07-01T00:00:00Z")
            .queryParam("completed", "false")
            .queryParam("page", 0)
            .queryParam("size", 10)
            .when()
            .get(RestAssured.baseURI + TASKS_PATH) // Uses the static variable for the path "/tasks"
            .then()
            .statusCode(200);
    }
}
