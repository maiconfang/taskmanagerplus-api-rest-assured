package com.maif.taskmanagerplus_api_rest_assured.auth;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import com.maif.taskmanagerplus_api_rest_assured.config.ConfigLoader;

/**
 * AuthUtil class provides utility methods for handling authentication.
 * This class manages the retrieval and usage of an authentication token for REST Assured requests.
 * 
 * Author: Maicon Fang
 * Date: 2024-06-19
 */
public class AuthUtil {
    private static String token;
    
    // Externalized base URL and token URL using ConfigLoader to fetch from application-test.properties
    private static final String BASE_URL = ConfigLoader.getProperty("auth.base.url");
    private static final String TOKEN_URL = ConfigLoader.getProperty("auth.token.url");
    
    // Externalized client credentials
    private static final String CLIENT_ID = ConfigLoader.getProperty("auth.client.id");
    private static final String CLIENT_SECRET = ConfigLoader.getProperty("auth.client.secret");

    // Externalized default user credentials
    private static final String USERNAME = ConfigLoader.getProperty("auth.username");
    private static final String PASSWORD = ConfigLoader.getProperty("auth.password");
    private static final String GRANT_TYPE = ConfigLoader.getProperty("auth.grant_type");

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
    
    
    public static RequestSpecification addTokenHeader(RequestSpecification requestSpec) {
        String authToken = getAuthToken();
        return requestSpec
            .header("Authorization", "Bearer " + authToken)
            .header("Content-Type", "application/json");
    }

    /**
     * Authenticates the user and retrieves the authentication token.
     * This method sets the authorization header for all requests made with REST Assured.
     */
    public static void authenticate() {
        Response response = given()
                .auth().preemptive()
                .basic(CLIENT_ID, CLIENT_SECRET) // Externalized credentials
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("username", USERNAME) // Externalized user credentials
                .formParam("password", PASSWORD)
                .formParam("grant_type", GRANT_TYPE)
                .when()
                .post(TOKEN_URL);

        token = response.jsonPath().getString("access_token");

        // Sets the authorization header for all requests made with RestAssured
        RestAssured.requestSpecification = given().header("Authorization", "Bearer " + token);
    }

    /**
     * Authenticates a user with the provided username and password to obtain an access token.
     * 
     * This method is used to authenticate a newly created user by exchanging their credentials
     * for an access token. The token is then stored globally for subsequent API requests.
     * 
     * @param username The username of the user to authenticate.
     * @param password The password of the user to authenticate.
     */
    public static void authenticateUser(String email, String password) {
        Response response = given()
                .auth().preemptive()
                .basic(CLIENT_ID, CLIENT_SECRET) // Externalized client credentials
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("username", email) // Dynamically provided user credentials
                .formParam("password", password)
                .formParam("grant_type", GRANT_TYPE)
                .when()
                .post(TOKEN_URL);

        token = response.jsonPath().getString("access_token");

        // Sets the authorization header for all requests made with RestAssured
        RestAssured.requestSpecification = given().header("Authorization", "Bearer " + token);
    }

}
