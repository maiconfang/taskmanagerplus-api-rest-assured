
package com.maif.taskmanagerplus_api_rest_assured.tests.users;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

import com.maif.taskmanagerplus_api_rest_assured.tests.base.BaseTest;
import com.maif.taskmanagerplus_api_rest_assured.tests.util.DatabaseInsertUtil;
import com.maif.taskmanagerplus_api_rest_assured.tests.util.TestUtil;

import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * Tests for the Users API endpoints using RestAssured.
 * These tests cover CRUD operations and various filters.
 * 
 * Author: Maicon Fang 
 * Date: 2024-06-23
 */

public class UserApiTest extends BaseTest {
    
    private static final String USERS_PATH = "/usserrs"; // Static variable for the path "/usserrs"
    private static final String PASSWORD_PATH = "/password";
    
    /**
     * Test case to create a new province via API.
     * It validates if the province is successfully created and checks its details.
     */
    @Test
    public void testCreateUser() {
        int id = -1; // Variable to store the ID of the created user

        try {
        	String requestBody = "{ \"name\": \"Maicon Alexander\", \"email\": \"maiconalexandermf@taskmanagerplus.com\", \"password\": \"$2y$12$NSsM4gEOR7MKogflKR7GMeYugkttjNhAJMvFdHrBLaLp2HzlggP5W\" }";

            Response response = given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
                .body(requestBody)
                .when()
                .post(RestAssured.baseURI + USERS_PATH) // Builds the complete URL using RestAssured.baseURI and USERS_PATH
                .then()
                .statusCode(201)
                .body("name", equalTo("Maicon Alexander"))
                .body("email", equalTo("maiconalexandermf@taskmanagerplus.com"))
                .extract().response(); // Extracts the HTTP response

            // Extract the ID of the created user from the JSON response
            id = response.path("id");
        } finally {
            // Ensure the user is deleted even if the test fails
            if (id != -1) {
                DatabaseInsertUtil.deleteUser(id);
            }
        }
    }

    /**
     * Test case to update an existing user via API.
     * It validates if the user is successfully updated with new details.
     */
    @Test
    public void testUpdateUser() {
    	int id = 0; // Inicialize
    	Timestamp dtCreate = java.sql.Timestamp.valueOf("2024-06-24 10:00:00");
    	Timestamp dtUpdate = java.sql.Timestamp.valueOf("2024-06-24 10:00:00");
    	String password = "$2y$12$NSsM4gEOR7MKogflKR7GMeYugkttjNhAJMvFdHrBLaLp2HzlggP5W";
    	

        try {
        	id = DatabaseInsertUtil.insertUser("Sophia Jones", "sophia.jones@taskmanagerplus.com", password, dtCreate, dtUpdate);
        	String requestBody = "{ \"id\": " + id + ", \"name\": \"Sophia Jones Updated\", \"email\": \"sophia.jones.mf@taskmanagerplus.com\"}";

            given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
                .body(requestBody)
                .when()
                .put(RestAssured.baseURI + USERS_PATH + "/" + id) // Builds the complete URL using RestAssured.baseURI and USERS_PATH
                .then()
                .statusCode(200)
                .body("name", equalTo("Sophia Jones Updated"))
                .body("email", equalTo("sophia.jones.mf@taskmanagerplus.com"));
        } finally {
            // Ensure the province is deleted even if the test fails
        	DatabaseInsertUtil.deleteUser(id);
        }
    }
    
    /**
     * Test to verify that a user cannot update another user's password.
     * 
     * This test verifies if the operation to update a user's password is correctly
     * protected by security permissions. It ensures that only the user who created
     * the account can successfully change the password. The test inserts a user into
     * the database with specific details and attempts to update the password using
     * another user's ID. It expects a 403 Forbidden status with a specific error message.
     */
    
    @Test
    public void testNotUpdatePasswordUser() {
    	int id = 0; // Inicialize
    	Timestamp dtCreate = java.sql.Timestamp.valueOf("2024-06-24 10:00:00");
    	Timestamp dtUpdate = java.sql.Timestamp.valueOf("2024-06-24 10:00:00");
    	String passwordFrom = "$2y$12$NSsM4gEOR7MKogflKR7GMeYugkttjNhAJMvFdHrBLaLp2HzlggP5W"; // 123456
    	String passwordTo = "$2a$12$ycxvFmxrGBiz5bPsmUzPH.03wwqmKTLME966YaMrhoucpQ3Dsmn9e"; //654321 

        try {
        	id = DatabaseInsertUtil.insertUser("Sophia Jones", "sophia.jones@taskmanagerplus.com", passwordFrom, dtCreate, dtUpdate);
        	
        	String requestBody = "{ \"currentPassword\": \"" + passwordFrom + "\", \"newPassword\": \"" + passwordTo + "\" }";

            given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
                .body(requestBody)
                .when()
                .put(RestAssured.baseURI + USERS_PATH + "/" + id + PASSWORD_PATH ) // Builds the complete URL using RestAssured.baseURI and USERS_PATH
                .then()
                .body("status", equalTo(403))
                .body("timestamp", notNullValue())
                .body("type", equalTo("http://localhost:8080/access-denied"))
                .body("title", equalTo("Access denied"))
                .body("detail", containsString("Access is denied"))
                .body("userMessage", equalTo("You do not have permission to perform this operation."));
        } finally {
            // Ensure the province is deleted even if the test fails
        	DatabaseInsertUtil.deleteUser(id);
        }
    }
    
    /**
     * Test to filter users by name.
     * 
     * This test verifies the functionality of filtering users by their name. It inserts
     * a user into the database with specific details, then makes a GET request to fetch
     * users filtered by the name "Charlotte Brown". It validates that the returned user
     * matches the expected name and email address.
     */
    
    @Test
    public void testFilterUsersByName() {
        int id = 0;
        
        Timestamp dtCreate = java.sql.Timestamp.valueOf("2024-06-24 10:00:00");
        Timestamp dtUpdate = java.sql.Timestamp.valueOf("2024-06-24 10:00:00");
    	String password = "$2y$12$NSsM4gEOR7MKogflKR7GMeYugkttjNhAJMvFdHrBLaLp2HzlggP5W";
        
        try {
            // Insert a user into the database
            id = DatabaseInsertUtil.insertUser("Charlotte Brown", "charlotte.brown@taskmanagerplus.com", password, dtCreate, dtUpdate);

            // Makes the GET request to fetch user filtered by name
            RestAssured.given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Adds the authorization header
                .queryParam("name", "Charlotte Brown")
                .when()
                .get(RestAssured.baseURI + USERS_PATH)
                .then()
                .statusCode(200)
                .body("_embedded.usserrs[0].name", equalTo("Charlotte Brown"))
                .body("_embedded.usserrs[0].email", equalTo("charlotte.brown@taskmanagerplus.com"));
                
        } finally {
            // Ensure that the inserted user is deleted from the database even if the test fails
            if (id != 0) {
                DatabaseInsertUtil.deleteUser(id);
            }
        }
    }
    
    /**
     * Test to filter users by email.
     * 
     * This test verifies the functionality of filtering users by their email address.
     * It inserts a user into the database with specific details, then makes a GET request
     * to fetch users filtered by the email "olivia.miller@taskmanagerplus.com". It validates
     * that the returned user matches the expected name and email address.
     */
    
    @Test
    public void testFilterUsersByEmail() {
        int id = 0;
        
        Timestamp dtCreate = java.sql.Timestamp.valueOf("2024-06-24 10:00:00");
        Timestamp dtUpdate = java.sql.Timestamp.valueOf("2024-06-24 10:00:00");
    	String password = "$2y$12$NSsM4gEOR7MKogflKR7GMeYugkttjNhAJMvFdHrBLaLp2HzlggP5W";
        
        try {
            // Insert a user into the database
            id = DatabaseInsertUtil.insertUser("Olivia Miller", "olivia.miller@taskmanagerplus.com", password, dtCreate, dtUpdate);

            // Makes the GET request to fetch user filtered by email
            RestAssured.given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Adds the authorization header
                .queryParam("email", "olivia.miller@taskmanagerplus.com")
                .when()
                .get(RestAssured.baseURI + USERS_PATH)
                .then()
                .statusCode(200)
                .body("_embedded.usserrs[0].name", equalTo("Olivia Miller"))
                .body("_embedded.usserrs[0].email", equalTo("olivia.miller@taskmanagerplus.com"));
                
        } finally {
            // Ensure that the inserted user is deleted from the database even if the test fails
            if (id != 0) {
                DatabaseInsertUtil.deleteUser(id);
            }
        }
    }
    
    
    /**
     * Test to filter users by name and email.
     * 
     * This test verifies the functionality of filtering users by both their name and
     * email address. It inserts a user into the database with specific details, then
     * makes a GET request to fetch users filtered by the name "Leo Johnson" and email
     * "leo.johnson@taskmanagerplus.com". It validates that the returned user matches
     * the expected name and email address.
     */

    
    @Test
    public void testFilterUsersByNameAndEmail() {
        int id = 0;
        
        Timestamp dtCreate = java.sql.Timestamp.valueOf("2024-06-24 10:00:00");
        Timestamp dtUpdate = java.sql.Timestamp.valueOf("2024-06-24 10:00:00");
    	String password = "$2y$12$NSsM4gEOR7MKogflKR7GMeYugkttjNhAJMvFdHrBLaLp2HzlggP5W";
        
        try {
            // Insert a user into the database
            id = DatabaseInsertUtil.insertUser("Leo Johnson", "leo.johnson@taskmanagerplus.com", password, dtCreate, dtUpdate);

            // Makes the GET request to fetch user filtered by name
            RestAssured.given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Adds the authorization header
                .queryParam("name", "Leo Johnson")
                .queryParam("email", "leo.johnson@taskmanagerplus.com")
                .when()
                .get(RestAssured.baseURI + USERS_PATH)
                .then()
                .statusCode(200)
                .body("_embedded.usserrs[0].name", equalTo("Leo Johnson"))
                .body("_embedded.usserrs[0].email", equalTo("leo.johnson@taskmanagerplus.com"));
                
        } finally {
            // Ensure that the inserted user is deleted from the database even if the test fails
            if (id != 0) {
                DatabaseInsertUtil.deleteUser(id);
            }
        }
    }
   
}
