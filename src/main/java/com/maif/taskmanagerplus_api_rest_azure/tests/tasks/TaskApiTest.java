package com.maif.taskmanagerplus_api_rest_azure.tests.tasks;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import com.maif.taskmanagerplus_api_rest_azure.tests.base.BaseTest;
import com.maif.taskmanagerplus_api_rest_azure.util.TestUtil;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TaskApiTest extends BaseTest {
    
    private static final String TASKS_PATH = "/tasks"; // Static variable for the path "/tasks"

    @Test
    public void testCreateTask() {
        String requestBody = "{ \"title\": \"New Task\", \"description\": \"New Task Description\", \"dueDate\": \"2024-06-30T00:00:00Z\", \"completed\": false }";

        given()
            .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
            .body(requestBody)
            .when()
            .post(RestAssured.baseURI + TASKS_PATH) // Builds the complete URL using RestAssured.baseURI and TASKS_PATH
            .then()
            .statusCode(201)
            .body("title", equalTo("New Task"));
    }
    
    @Test
    public void testDeleteTask() {

        // Create a task to ensure there is something to delete
        String requestBody = "{ \"title\": \"Task to Delete\", \"description\": \"Task Description\", \"dueDate\": \"2024-06-30T00:00:00Z\", \"completed\": false }";

        // Send the creation request and log the details
        Response createResponse = given()
            .spec(TestUtil.addTokenHeader(RestAssured.given()))
            .body(requestBody)
//            .log().all() // Log all request details
            .when()
            .post(RestAssured.baseURI + TASKS_PATH)
            .then()
//            .log().all() // Log all response details
            .statusCode(201)
            .extract().response();

        // Extract the ID of the created task
        int taskIdNew = createResponse.path("id");

        // Send the deletion request and log the details
        given()
            .spec(TestUtil.addTokenHeader(RestAssured.given()))
            .log().all() // Log all request details
            .when()
            .delete(RestAssured.baseURI + TASKS_PATH + "/" + taskIdNew)
            .then()
            .log().all() // Log all response details
            .statusCode(204);
    }
    
   @Test
    public void testGetTask() {
        int taskId = 1; // Replace with the actual task ID

        given()
            .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
            .when()
            .get(RestAssured.baseURI + TASKS_PATH + "/" + taskId) // Builds the complete URL using RestAssured.baseURI and TASKS_PATH
            .then()
            .statusCode(200)
            .body("id", equalTo(taskId));
    }
    
   @Test
    public void testUpdateTask() {
        int taskId = 1; // Replace with the actual task ID
        String requestBody = "{ \"id\": " + taskId + ", \"title\": \"Updated Task\", \"description\": \"Updated Description\", \"dueDate\": \"2024-07-01T00:00:00Z\", \"completed\": true }";

        given()
            .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
            .body(requestBody)
            .when()
            .put(RestAssured.baseURI + TASKS_PATH + "/" + taskId) // Builds the complete URL using RestAssured.baseURI and TASKS_PATH
            .then()
            .statusCode(200)
            .body("title", equalTo("Updated Task"));
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
    
    @Test
    public void testHelloWorld() {
        given()
            .when()
            .get(RestAssured.baseURI + "/tasks/hello")
            .then()
            .statusCode(200)
            .body(equalTo("Hello World!"));
    }
    

}
