package com.maif.taskmanagerplus_api_rest_assured.tests.tasks;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalTo;

import java.util.Date;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maif.taskmanagerplus_api_rest_assured.model.TaskDTO;
import com.maif.taskmanagerplus_api_rest_assured.tests.base.BaseTest;
import com.maif.taskmanagerplus_api_rest_assured.tests.util.DatabaseInsertUtil;
import com.maif.taskmanagerplus_api_rest_assured.tests.util.TestUtil;

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
    public void testCreateTask() throws JsonProcessingException {
    	
    	// Create a TaskDTO object and set its fields
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("New Task");
        taskDTO.setDescription("New Task Description");
        taskDTO.setDueDate("2024-06-30T00:00:00Z");
        taskDTO.setCompleted(false);

        // Convert the TaskDTO object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(taskDTO);
        
        int taskId = 0; // Inicialize taskId
        
        try {
            // Perform POST request to create a task
            Response response = given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
                .body(requestBody)
                .when()
                .post(RestAssured.baseURI + TASKS_PATH) // Builds the complete URL using RestAssured.baseURI and TASKS_PATH
                .then()
                .statusCode(201)
                .body("title", equalTo("New Task"))
                .extract().response(); // Extracts the HTTP response

            // Extract the ID of the created task from the JSON response
            taskId = response.path("id");
        } finally {
            // Ensure that the created task is deleted from the database even if the test fails
            if (taskId != 0) {
                DatabaseInsertUtil.deleteTask(taskId);
            }
        }
    }

    
    @Test
    public void testDeleteTask() {
    	
    	// Insert a task into the database and get the ID
    	Date dueDate = java.sql.Timestamp.valueOf("2024-06-30 00:00:00");
    	 
        int taskIdNew = DatabaseInsertUtil.insertTask("Task to Delete", "Task Description", dueDate, false);

        // Send the deletion request and log the details
        given()
            .spec(TestUtil.addTokenHeader(RestAssured.given()))
            .when()
            .delete(RestAssured.baseURI + TASKS_PATH + "/" + taskIdNew)
            .then()
            .statusCode(204);
    }
    
    @Test
    public void testGetTask() {
        int taskIdGet = 0; // Inicialize taskIdGet

        try {
            // Insert a task into the database
            Date dueDate = java.sql.Timestamp.valueOf("2024-06-30 00:00:00");
            taskIdGet = DatabaseInsertUtil.insertTask("Task to Get", "Task Description Get", dueDate, false);

            // Perform GET request to retrieve the task
            given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
                .when()
                .get(RestAssured.baseURI + TASKS_PATH + "/" + taskIdGet) // Builds the complete URL using RestAssured.baseURI and TASKS_PATH
                .then()
                .statusCode(200)
                .body("id", equalTo(taskIdGet));
        } finally {
            // Ensure that the inserted task is deleted from the database even if the test fails
            if (taskIdGet != 0) {
                DatabaseInsertUtil.deleteTask(taskIdGet);
            }
        }
    }

    
    @Test
    public void testUpdateTask() {
        int taskIdUpdate = 0; // Inicialize taskIdUpdate
        
        try {
            // Insert a task into the database
            Date dueDate = java.sql.Timestamp.valueOf("2024-06-20 10:00:00");
            taskIdUpdate = DatabaseInsertUtil.insertTask("Task will be updated", "Task Description will be updated", dueDate, false);
            
            // Prepare the request body for updating the task
            String requestBody = "{ \"id\": " + taskIdUpdate + ", \"title\": \"Updated Task\", \"description\": \"Updated Description\", \"dueDate\": \"2024-07-01T00:00:00Z\", \"completed\": true }";

            // Perform PUT request to update the task
            given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
                .body(requestBody)
                .when()
                .put(RestAssured.baseURI + TASKS_PATH + "/" + taskIdUpdate) // Builds the complete URL using RestAssured.baseURI and TASKS_PATH
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated Task"));
        } finally {
            // Ensure that the updated task is deleted from the database even if the test fails
            if (taskIdUpdate != 0) {
                DatabaseInsertUtil.deleteTask(taskIdUpdate);
            }
        }
    }

    
    
    @Test
    public void testFilterIdWithPaginationTask() {
        int taskIdFilterIdWithPag = 0; // Inicialize taskIdFilterIdWithPag
        
        try {
            // Insert a task into the database
            Date dueDate = java.sql.Timestamp.valueOf("2024-06-20 16:15:20");
            taskIdFilterIdWithPag = DatabaseInsertUtil.insertTask("Task to FilterIdWithPagination", "Task Description FilterIdWithPagination", dueDate, false);

            // Perform GET request with query parameters
            given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
                .queryParam("taskId", taskIdFilterIdWithPag)
                .queryParam("completed", "false")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get(RestAssured.baseURI + TASKS_PATH) // Uses the static variable for the path "/tasks"
                .then()
                .statusCode(200)
                .body("_embedded.tasks[0].id", equalTo(taskIdFilterIdWithPag))
                .body("_embedded.tasks[0].title", equalTo("Task to FilterIdWithPagination"))
                .body("_embedded.tasks[0].description", equalTo("Task Description FilterIdWithPagination"))
                .body("_embedded.tasks[0].dueDate", equalTo("2024-06-20T16:15:20Z"))
                .body("_embedded.tasks[0].completed", equalTo(false));
        } finally {
            // Ensure that the inserted task is deleted from the database even if the test fails
            if (taskIdFilterIdWithPag != 0) {
                DatabaseInsertUtil.deleteTask(taskIdFilterIdWithPag);
            }
        }
    }

    
    @Test
    public void testFilterTitleWithPaginationTask() {
        int taskIdFilterTitleWithPag = 0; // Inicialize taskIdFilterTitleWithPag
        
        try {
            // Insert a task into the database
            Date dueDate = java.sql.Timestamp.valueOf("2024-06-20 15:40:20");
            taskIdFilterTitleWithPag = DatabaseInsertUtil.insertTask("Task to TitleWithPagination", "Task Description TitleWithPagination", dueDate, false);

            // Perform GET request with query parameters
            given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
                .queryParam("title", "Task to TitleWithPagination")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get(RestAssured.baseURI + TASKS_PATH) // Uses the static variable for the path "/tasks"
                .then()
                .statusCode(200)
                .body("_embedded.tasks[0].title", equalTo("Task to TitleWithPagination"))
                .body("_embedded.tasks[0].description", equalTo("Task Description TitleWithPagination"))
                .body("_embedded.tasks[0].dueDate", equalTo("2024-06-20T15:40:20Z"))
                .body("_embedded.tasks[0].completed", equalTo(false));
        } finally {
            // Ensure that the inserted task is deleted from the database even if the test fails
            if (taskIdFilterTitleWithPag != 0) {
                DatabaseInsertUtil.deleteTask(taskIdFilterTitleWithPag);
            }
        }
    }

    
    @Test
    public void testFilterDescriptionWithPaginationTask() {
        int taskIdFilterDescriptionWithPag = 0; // Inicialize taskIdFilterDescriptionWithPag
        
        try {
            // Insert a task into the database
            Date dueDate = java.sql.Timestamp.valueOf("2024-06-20 15:48:30");
            taskIdFilterDescriptionWithPag = DatabaseInsertUtil.insertTask("Task to FilterDescriptionWithPagination", "Task Description FilterDescriptionWithPagination", dueDate, true);

            // Perform GET request with query parameters
            given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
                .queryParam("description", "Task Description FilterDescriptionWithPagination")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get(RestAssured.baseURI + TASKS_PATH) // Uses the static variable for the path "/tasks"
                .then()
                .statusCode(200)
                .body("_embedded.tasks[0].title", equalTo("Task to FilterDescriptionWithPagination"))
                .body("_embedded.tasks[0].description", equalTo("Task Description FilterDescriptionWithPagination"))
                .body("_embedded.tasks[0].dueDate", equalTo("2024-06-20T15:48:30Z"))
                .body("_embedded.tasks[0].completed", equalTo(true));
        } finally {
            // Ensure that the inserted task is deleted from the database even if the test fails
            if (taskIdFilterDescriptionWithPag != 0) {
                DatabaseInsertUtil.deleteTask(taskIdFilterDescriptionWithPag);
            }
        }
    }

    
    @Test
    public void testFilterDueDateWithPaginationTask() {
        int taskIdFilterDueDateWithPag = 0; // Inicialize taskIdFilterDueDateWithPag
        
        try {
            // Insert a task into the database
            Date dueDate = java.sql.Timestamp.valueOf("2024-06-20 16:02:10");
            taskIdFilterDueDateWithPag = DatabaseInsertUtil.insertTask("Task to FilterDueDateWithPagination", "Task Description FilterDueDateWithPagination", dueDate, true);

            // Perform GET request with query parameters
            given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
                .queryParam("dueDate", "2024-06-20T16:02:10Z")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get(RestAssured.baseURI + TASKS_PATH) // Uses the static variable for the path "/tasks"
                .then()
                .statusCode(200)
                .body("_embedded.tasks[0].title", equalTo("Task to FilterDueDateWithPagination"))
                .body("_embedded.tasks[0].description", equalTo("Task Description FilterDueDateWithPagination"))
                .body("_embedded.tasks[0].dueDate", equalTo("2024-06-20T16:02:10Z"))
                .body("_embedded.tasks[0].completed", equalTo(true));
        } finally {
            // Ensure that the inserted task is deleted from the database even if the test fails
            if (taskIdFilterDueDateWithPag != 0) {
                DatabaseInsertUtil.deleteTask(taskIdFilterDueDateWithPag);
            }
        }
    }

    
    
    @Test
    public void testFilterCompletedAndTitleWithPaginationTask() {
        int taskIdFilterCompletedAndTitleWithPag = 0; // Inicialize taskIdFilterCompletedAndTitleWithPag
        
        try {
            // Insert a task into the database
            Date dueDate = java.sql.Timestamp.valueOf("2024-06-20 16:12:10");
            taskIdFilterCompletedAndTitleWithPag = DatabaseInsertUtil.insertTask("Task to FilterCompletedAndTitle", "Task Description FilterCompletedAndTitle", dueDate, false);

            // Perform GET request with query parameters
            given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
                .queryParam("completed", "false")
                .queryParam("title", "Task to FilterCompletedAndTitle")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get(RestAssured.baseURI + TASKS_PATH) // Uses the static variable for the path "/tasks"
                .then()
                .statusCode(200)
                .body("_embedded.tasks[0].title", equalTo("Task to FilterCompletedAndTitle"))
                .body("_embedded.tasks[0].description", equalTo("Task Description FilterCompletedAndTitle"))
                .body("_embedded.tasks[0].dueDate", equalTo("2024-06-20T16:12:10Z"))
                .body("_embedded.tasks[0].completed", equalTo(false));
        } finally {
            // Ensure that the inserted task is deleted from the database even if the test fails
            if (taskIdFilterCompletedAndTitleWithPag != 0) {
                DatabaseInsertUtil.deleteTask(taskIdFilterCompletedAndTitleWithPag);
            }
        }
    }

    
    @Test
    public void testFilterIdAndTitleAndDescriptionAndDueDateAndCompletedWithPaginationTask() {
        int taskId = 0; // Inicialize taskId
        
        try {
            // Insert a task into the database
            Date dueDate = java.sql.Timestamp.valueOf("2024-06-20 16:21:20");
            taskId = DatabaseInsertUtil.insertTask("Task to TitleDescriptionDueDate", "Task Description TitleDescriptionDueDate", dueDate, false);

            // Perform GET request with query parameters
            given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
                .queryParam("taskId", taskId)
                .queryParam("title", "Task to TitleDescriptionDueDate")
                .queryParam("description", "Task Description TitleDescriptionDueDate")
                .queryParam("dueDate", "2024-06-20T16:21:20Z")
                .queryParam("completed", "false")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get(RestAssured.baseURI + TASKS_PATH) // Uses the static variable for the path "/tasks"
                .then()
                .statusCode(200)
                .body("_embedded.tasks[0].id", equalTo(taskId))
                .body("_embedded.tasks[0].title", equalTo("Task to TitleDescriptionDueDate"))
                .body("_embedded.tasks[0].description", equalTo("Task Description TitleDescriptionDueDate"))
                .body("_embedded.tasks[0].dueDate", equalTo("2024-06-20T16:21:20Z"))
                .body("_embedded.tasks[0].completed", equalTo(false));
        } finally {
            // Ensure that the inserted task is deleted from the database even if the test fails
            if (taskId != 0) {
                DatabaseInsertUtil.deleteTask(taskId);
            }
        }
    }

    
    @Test
    public void testFilterTasksByTitleNoPagination() {
        int taskId = 0; // Inicialize taskId
        
        try {
            // Insert a task into the database
            Date dueDate = java.sql.Timestamp.valueOf("2024-06-20 16:25:35");
            taskId = DatabaseInsertUtil.insertTask("Task to FilterTasksByTitleNoPagination", "Task Description FilterTasksByTitleNoPagination", dueDate, false);

            // Makes the GET request to fetch tasks filtered by title
            RestAssured.given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Adds the authorization header
                .queryParam("title", "Task to FilterTasksByTitleNoPagination")
                .when()
                .get(RestAssured.baseURI + TASKS_PATH + TASKS_PATH_NOPAGINATION)
                .then()
                .statusCode(200)
                .body("_embedded.tasks[0].title", equalTo("Task to FilterTasksByTitleNoPagination")) // Checks if the title of the first task in the result is "Task to FilterTasksByTitleNoPagination"
                .body("_embedded.tasks[0].description", equalTo("Task Description FilterTasksByTitleNoPagination")) // Checks the description
                .body("_embedded.tasks[0].dueDate", equalTo("2024-06-20T16:25:35Z")) // Checks the due date
                .body("_embedded.tasks[0].completed", equalTo(false)) // Checks if it is not completed
                .body("page", nullValue()); // Checks if the "page" key is not present in the JSON response
        } finally {
            // Ensure that the inserted task is deleted from the database even if the test fails
            if (taskId != 0) {
                DatabaseInsertUtil.deleteTask(taskId);
            }
        }
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

    
    /**
     * Test to verify the response of the "/tasks/hello" endpoint returning "Hello World!".
     * 
     * Objective:
     * - Verify that the API correctly responds with "Hello World!" when accessing the "/tasks/hello" endpoint.
     * - This test does not require authentication or special permissions as it is a simple test to verify basic API functionality.
     * 
     * Corresponding API Method:
     * - Endpoint: GET "/hello"
     * - Method: GET
     * - Returns: "Hello World!"
     */
    @Test
    public void testHelloWorld() {
        given()
            .when()
            .get(RestAssured.baseURI + "/tasks/hello")
            .then()
            .statusCode(200) // Verifies if the response status code is 200 OK
            .body(equalTo("Hello World!")); // Verifies if the response body is exactly "Hello World!"
    }

    
    /**
     * Test case to verify the behavior when retrieving a non-existent task.
     * 
     * Scenario:
     * - Perform a GET request for a task with a non-existent ID.
     * - Expect a 404 Not Found response with specific error details.
     * - Ensure the error response body matches the expected structure.
     */
    @Test
    public void testGetNonExistentTask() {
        // ID of a task that does not exist in the database
        Long nonExistentTaskId = 55L;

        // Perform GET request to retrieve the non-existent task
        given()
            .spec(TestUtil.addTokenHeader(RestAssured.given())) // Adds the authorization header using a helper method
            .when()
            .get(RestAssured.baseURI + TASKS_PATH + "/" + nonExistentTaskId) // Builds the complete URL using RestAssured.baseURI and TASKS_PATH
            .then()
            .statusCode(404) // Verifies the response status code is 404 Not Found
            .body("status", equalTo(404)) // Verifies the "status" field in the response body is 404
            .body("timestamp", notNullValue()) // Verifies the "timestamp" field is not null
            .body("type", equalTo("http://localhost:8080/resource-not-found")) // Verifies the "type" field is the expected error type
            .body("title", equalTo("Resource not found")) // Verifies the "title" field is the expected error title
            .body("detail", equalTo("There is no register of the task with a code " + nonExistentTaskId)) // Verifies the "detail" field is the expected error detail message
            .body("userMessage", equalTo("There is no register of the task with a code " + nonExistentTaskId)); // Verifies the "userMessage" field is the expected user-friendly error message
    }

    

}
