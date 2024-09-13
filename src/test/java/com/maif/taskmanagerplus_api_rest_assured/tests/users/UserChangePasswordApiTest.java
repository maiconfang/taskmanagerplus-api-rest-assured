
package com.maif.taskmanagerplus_api_rest_assured.tests.users;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.sql.Timestamp;

import com.maif.taskmanagerplus_api_rest_assured.auth.AuthUtil;
import com.maif.taskmanagerplus_api_rest_assured.tests.util.DataBaseInsertUtil;
import com.maif.taskmanagerplus_api_rest_assured.tests.util.TestUtil;

import io.restassured.RestAssured;

/**
 * Tests for the Users API endpoints using RestAssured.
 * These tests cover CRUD operations and various filters.
 * 
 * Author: Maicon Fang
 * Date: 2024-06-23
 */

public class UserChangePasswordApiTest {
    
    private static final String USERS_PATH = "/usserrs"; // Static variable for the path "/usserrs"
    private static final String PASSWORD_PATH = "/password";
    

    /**
     * Test to update user password.
     * 
     * This test verifies the functionality to update a user's password. Note that this test
     * is designed for version 2, as it requires configuration or creation of a class to generate
     * the correct password recognized by the API.
     * 
     * In version 2:
     * - A class/method will be configured/created to generate the correct password format that
     *   the API recognizes as equal.
     */
    
//    @Test
    public void shouldUpdateUserPasswordSuccessfully() {
    	int id = 0; // Inicialize
    	Timestamp dtCreate = java.sql.Timestamp.valueOf("2024-06-24 10:00:00");
    	Timestamp dtUpdate = java.sql.Timestamp.valueOf("2024-06-24 10:00:00");
    	
    	String passwordFrom = "$2a$12$RZe45hE/QygoOhK80mo5lOrbn1MVYOrv0lMyacIEI4rufXnKn1D.G"; // 123456
    	String passwordCurrent = "123456";
    	String passwordTo = "$2a$12$ZMoq9AiXZZdIBsqyL6HGpu299rO9kchJy2CGcwwntyZGQKH.PqZqO"; //654321 

        try {
        	id = DataBaseInsertUtil.insertUser("Sophia Jones", "sophia.jones@taskmanagerplus.com", passwordFrom, dtCreate, dtUpdate);
        	
        	AuthUtil.authenticateUser("sophia.jones@taskmanagerplus.com", "123456");
        	
        	String requestBody = "{ \"currentPassword\": \"$2a$12$RZe45hE/QygoOhK80mo5lOrbn1MVYOrv0lMyacIEI4rufXnKn1D.G\", \"newPassword\": \"654321\" }";

            given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
                .body(requestBody)
                .when()
                .put(RestAssured.baseURI + USERS_PATH + "/" + id + PASSWORD_PATH ) // Builds the complete URL using RestAssured.baseURI and USERS_PATH
                .then()
                .body("status", equalTo(204));
        } finally {
            // Ensure the province is deleted even if the test fails
        	DataBaseInsertUtil.deleteUser(id);
        }
    }
    
     
}
