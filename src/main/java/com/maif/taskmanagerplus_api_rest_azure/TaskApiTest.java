package com.maif.taskmanagerplus_api_rest_azure;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.equalTo;


public class TaskApiTest {
	
	
	 private static String token;
	
	 @BeforeAll
	    public static void setup() {
	        RestAssured.baseURI = "http://localhost:8080/v1";
	        token = getAuthToken();
	    }
	 
	    private static String getAuthToken() {
	        Response response = given()
	            .auth()
	            .preemptive()
	            .basic("maif-web", "web123")
	            .header("Content-Type", "application/x-www-form-urlencoded")
	            .formParam("username", "luna.moon@maif.com")
	            .formParam("password", "123")
	            .formParam("grant_type", "password")
	            .when()
	            .post("http://localhost:8080/oauth/token");

	        response.then().statusCode(200);

	        return response.jsonPath().getString("access_token");
	    }
	    
	    @BeforeEach
	    public void authenticate() {
	        RestAssured.requestSpecification = given().header("Authorization", "Bearer " + token);
	    }

	    @Test
	    public void testCreateTask() {
	        String requestBody = "{ \"title\": \"New Task\", \"description\": \"New Task Description\", \"dueDate\": \"2024-06-30T00:00:00Z\", \"completed\": false }";

	        Response response = given()
	            .header("Content-Type", "application/json")
	            .header("Authorization", "Bearer " + token)
	            .body(requestBody)
	            .when()
	            .post("/tasks")
	            .then()
	            .statusCode(201)
	            .body("title", equalTo("New Task"))
	            .extract().response();
	        
	        
	    }

	    @Test
	    public void testGetTask() {
	        int taskId = 1; // replace with actual task id

	        given()
	            .when()
	            .get("/tasks/" + taskId)
	            .then()
	            .statusCode(200)
	            .body("id", equalTo(taskId));
	    }

	    @Test
	    public void testUpdateTask() {
	        int taskId = 1; // replace with actual task id
	        String requestBody = "{ \"title\": \"Updated Task\", \"description\": \"Updated Description\", \"dueDate\": \"2024-07-01T00:00:00Z\", \"completed\": true }";

	        given()
	            .header("Content-Type", "application/json")
	            .body(requestBody)
	            .when()
	            .put("/tasks/" + taskId)
	            .then()
	            .statusCode(200)
	            .body("title", equalTo("Updated Task"));
	    }

	    @Test
	    public void testDeleteTask() {
	        int taskId = 1; // replace with actual task id

	        given()
	            .when()
	            .delete("/tasks/" + taskId)
	            .then()
	            .statusCode(204);
	    }

	    @Test
	    public void testFilterTasks() {
	        given()
	            .queryParam("dueDate", "2024-07-01T00:00:00Z")
	            .queryParam("completed", "false")
	            .queryParam("page", 0)
	            .queryParam("size", 10)
	            .when()
	            .get("/tasks")
	            .then()
	            .statusCode(200);
	    }
	    
	    @Test
		public void testHelloWorld() {
		    given()
		        .when()
		        .get("/tasks/hello")
		        .then()
		        .statusCode(200)
		        .body(equalTo("Hello World!"));
		}

}
