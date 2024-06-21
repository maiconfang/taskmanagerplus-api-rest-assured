package com.maif.taskmanagerplus_api_rest_azure.auth;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;

/**
 * AuthUtil class provides utility methods for handling authentication.
 * This class manages the retrieval and usage of an authentication token for REST Assured requests.
 * 
 * Author: Maicon Fang
 * Date: 2024-06-19
 */
public class AuthUtil {
    private static String token;
    private static final String BASE_URL = "http://localhost:8080/v1";

    /**
     * Gets the base URL for the API.
     * 
     * @return The base URL as a string.
     */
    public static String getBaseUrl() {
        return BASE_URL;
    }

    /**
     * Gets the authentication token.
     * If the token is not already set, it calls the authenticate method to obtain it.
     * 
     * @return The authentication token as a string.
     */
    public static String getAuthToken() {
        if (token == null) {
            authenticate(); // Calls the authenticate() method to ensure the token is obtained and set correctly
        }
        return token;
    }

    /**
     * Authenticates the user and retrieves the authentication token.
     * This method sets the authorization header for all requests made with REST Assured.
     */
    public static void authenticate() {
        String tokenUrl = "/oauth/token";

        Response response = given()
                .auth().preemptive()
                .basic("maif-web", "web123")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("username", "luna.moon@maif.com")
                .formParam("password", "123")
                .formParam("grant_type", "password")
                .when()
                .post(tokenUrl);

        token = response.jsonPath().getString("access_token");

        // Sets the authorization header for all requests made with RestAssured
        RestAssured.requestSpecification = given().header("Authorization", "Bearer " + token);
    }
}
