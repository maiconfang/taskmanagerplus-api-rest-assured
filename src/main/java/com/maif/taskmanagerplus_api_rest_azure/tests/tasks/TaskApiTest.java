package com.maif.taskmanagerplus_api_rest_azure.tests.tasks;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import com.maif.taskmanagerplus_api_rest_azure.tests.base.BaseTest;
import com.maif.taskmanagerplus_api_rest_azure.util.TestUtil;

import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * Tests for the Task API endpoints using RestAssured.
 * These tests cover CRUD operations and various filters.
 * 
 * Author: Maicon Fang
 * Date: 2024-06-19
 * 
 */
public class TaskApiTest extends BaseTest {
    
    private static final String TASKS_PATH = "/tasks"; // Static variable for the path "/tasks"
    private static final String TASKS_PATH_NOPAGINATION = "/noPagination";

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
//            .log().all() // Log all request details
            .when()
            .delete(RestAssured.baseURI + TASKS_PATH + "/" + taskIdNew)
            .then()
//            .log().all() // Log all response details
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
    public void testFilterTitleWithPaginationTask() {
    	
    	given()
        .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
        .queryParam("title", "Task 2")
        .queryParam("page", 0)
        .queryParam("size", 10)
        .when()
        .get(RestAssured.baseURI + TASKS_PATH) // Uses the static variable for the path "/tasks"
        .then()
        .statusCode(200)
//        .log().all()
        .body("_embedded.tasks[0].title", equalTo("Task 2"))
        .body("_embedded.tasks[0].description", equalTo("Description for Task 2"))
        .body("_embedded.tasks[0].dueDate", equalTo("2024-07-01T00:00:00Z"))
        .body("_embedded.tasks[0].completed", equalTo(false));

    }
    
    @Test
    public void testFilterDescriptionWithPaginationTask() {
    	
    	given()
        .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
        .queryParam("description", "Description for Task 2")
        .queryParam("page", 0)
        .queryParam("size", 10)
        .when()
        .get(RestAssured.baseURI + TASKS_PATH) // Uses the static variable for the path "/tasks"
        .then()
        .statusCode(200)
        .body("_embedded.tasks[0].title", equalTo("Task 2"))
        .body("_embedded.tasks[0].description", equalTo("Description for Task 2"))
        .body("_embedded.tasks[0].dueDate", equalTo("2024-07-01T00:00:00Z"))
        .body("_embedded.tasks[0].completed", equalTo(false));

    }
    
    @Test
    public void testFilterDueDateWithPaginationTask() {
    	
    	given()
        .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
        .queryParam("dueDate", "2024-07-01T00:00:00Z")
        .queryParam("page", 0)
        .queryParam("size", 10)
        .when()
        .get(RestAssured.baseURI + TASKS_PATH) // Uses the static variable for the path "/tasks"
        .then()
        .statusCode(200)
        .body("_embedded.tasks[0].title", equalTo("Task 2"))
        .body("_embedded.tasks[0].description", equalTo("Description for Task 2"))
        .body("_embedded.tasks[0].dueDate", equalTo("2024-07-01T00:00:00Z"))
        .body("_embedded.tasks[0].completed", equalTo(false));

    }
    
    @Test
    public void testFilterCompletedWithPaginationTask() {
    	
    	given()
        .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
        .queryParam("dueDate", "2024-07-01T00:00:00Z")
        .queryParam("page", 0)
        .queryParam("size", 10)
        .when()
        .get(RestAssured.baseURI + TASKS_PATH) // Uses the static variable for the path "/tasks"
        .then()
        .statusCode(200)
        .body("_embedded.tasks[0].title", equalTo("Task 2"))
        .body("_embedded.tasks[0].description", equalTo("Description for Task 2"))
        .body("_embedded.tasks[0].dueDate", equalTo("2024-07-01T00:00:00Z"))
        .body("_embedded.tasks[0].completed", equalTo(false));

    }
    
    @Test
    public void testFilterCompletedAndTitleWithPaginationTask() {
    	
    	given()
        .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
        .queryParam("completed", "false")
        .queryParam("title", "Task 2")
        .queryParam("page", 0)
        .queryParam("size", 10)
        .when()
        .get(RestAssured.baseURI + TASKS_PATH) // Uses the static variable for the path "/tasks"
        .then()
        .statusCode(200)
        .body("_embedded.tasks[0].title", equalTo("Task 2"))
        .body("_embedded.tasks[0].description", equalTo("Description for Task 2"))
        .body("_embedded.tasks[0].dueDate", equalTo("2024-07-01T00:00:00Z"))
        .body("_embedded.tasks[0].completed", equalTo(false));

    }
    
    @Test
    public void testFilterTasksByTitleNoPagination() {
        String expectedTitle = "Task 2";

        // Makes the GET request to fetch tasks filtered by title
        RestAssured.given()
            .spec(TestUtil.addTokenHeader(RestAssured.given())) // Adds the authorization header
            .queryParam("title", expectedTitle)
            .when()
            .get(RestAssured.baseURI + TASKS_PATH + TASKS_PATH_NOPAGINATION)
            .then()
            .statusCode(200)
            .body("_embedded.tasks[0].title", equalTo(expectedTitle)) // Checks if the title of the first task in the result is "Task 2"
            .body("_embedded.tasks[0].description", equalTo("Description for Task 2")) // Checks the description
            .body("_embedded.tasks[0].dueDate", equalTo("2024-07-01T00:00:00Z")) // Checks the due date
            .body("_embedded.tasks[0].completed", equalTo(false)) // Checks if it is not completed
            .body("page", nullValue()); // Checks if the "page" key is not present in the JSON response
    }
    
    @Test
    public void testCreateTitleMaxCaractereTask() {
    	String textTitle = "A simple task management system that allows users to create, update, delete, and mark tasks as completed. A simple task management system that "
    			+ "allows users to create, update, delete, and mark tasks as completed. A simple task management system that allows u";
    	String requestBody = "{ \"title\": \"" + textTitle + "\", \"description\": \"New Task Description\", \"dueDate\": \"2024-06-30T00:00:00Z\", \"completed\": false }";

    	 given()
         .spec(TestUtil.addTokenHeader(RestAssured.given()))
         .body(requestBody)
         .when()
         .post(RestAssured.baseURI + TASKS_PATH)
         .then()
         .statusCode(400)
         .body("status", equalTo(400))
         .body("timestamp", notNullValue())
         .body("type", equalTo("http://localhost:8080/max-length"))
         .body("title", equalTo("Maximum length exceeded"))
         .body("detail", containsString("Data too long for column 'title'"))
         .body("userMessage", equalTo("An unexpected internal system error has occurred. Please try again and if the problem persists, contact your system administrator"));
    }
    
    
    @Test
    public void testCreateDuoDateInvalidTask() {
    	  String requestBody = "{ \"title\": \"New Task\", \"description\": \"New Task Description\", \"dueDate\": \"hii2024-06-30T00:00:00Z\", \"completed\": false }";

    	  given()
          .spec(TestUtil.addTokenHeader(RestAssured.given()))
          .body(requestBody)
          .when()
          .post(RestAssured.baseURI + TASKS_PATH)
          .then()
          .statusCode(400)
          .body("status", equalTo(400))
          .body("timestamp", notNullValue())
          .body("type", equalTo("http://localhost:8080/invalid-request-body"))
          .body("title", equalTo("Invalid request body. Check the format of all fields and try again."))
          .body("detail", equalTo("Failed to parse date value in request body. Check date format and try again."))
          .body("userMessage", equalTo("Failed to parse date value 'hii2024-06-30T00:00:00Z'. Please use ISO-8601 format (e.g., 'yyyy-MM-dd'T'HH:mm:ss'Z')."));
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
