
package com.maif.taskmanagerplus_api_rest_assured.tests.provinces;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import com.maif.taskmanagerplus_api_rest_assured.tests.base.BaseTest;
import com.maif.taskmanagerplus_api_rest_assured.tests.util.DatabaseInsertUtil;
import com.maif.taskmanagerplus_api_rest_assured.tests.util.TestUtil;

import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * Tests for the Provinces API endpoints using RestAssured.
 * These tests cover CRUD operations and various filters.
 * 
 * Author: Maicon Fang
 * Date: 2024-06-21
 */
public class ProvinceApiTest extends BaseTest {
    
    private static final String PROVINCES_PATH = "/provinces"; // Static variable for the path "/provinces"
    private static final String PROVINCES_PATH_NOPAGINATION = "/noPagination";
    
    /**
     * Test case to create a new province via API.
     * It validates if the province is successfully created and checks its details.
     */
    @Test
    public void testCreateProvince() {
        int id = -1; // Variable to store the ID of the created province

        try {
            String requestBody = "{ \"name\": \"Provinces Teste mf\", \"abbreviation\": \"MF\" }";

            Response response = given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
                .body(requestBody)
                .when()
                .post(RestAssured.baseURI + PROVINCES_PATH) // Builds the complete URL using RestAssured.baseURI and PROVINCES_PATH
                .then()
                .statusCode(201)
                .body("name", equalTo("Provinces Teste mf"))
                .body("abbreviation", equalTo("MF"))
                .extract().response(); // Extracts the HTTP response

            // Extract the ID of the created province from the JSON response
            id = response.path("id");
        } finally {
            // Ensure the province is deleted even if the test fails
            if (id != -1) {
                DatabaseInsertUtil.deleteProvince(id);
            }
        }
    }

    /**
     * Test case to update an existing province via API.
     * It validates if the province is successfully updated with new details.
     */
    @Test
    public void testUpdateProvince() {
        int id = DatabaseInsertUtil.insertProvince("Provinces Teste Maif", "MF");

        try {
            String requestBody = "{ \"id\": \"" + id + "\", \"name\": \"Provinces Teste Maif Updated\", \"abbreviation\": \"UP\" }";

            given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
                .body(requestBody)
                .when()
                .put(RestAssured.baseURI + PROVINCES_PATH + "/" + id) // Builds the complete URL using RestAssured.baseURI and PROVINCES_PATH
                .then()
                .statusCode(200)
                .body("name", equalTo("Provinces Teste Maif Updated"))
                .body("abbreviation", equalTo("UP"));
        } finally {
            // Ensure the province is deleted even if the test fails
            DatabaseInsertUtil.deleteProvince(id);
        }
    }

    /**
     * Test case to delete a province via API.
     * It validates if the deletion request returns a successful status code.
     */
    @Test
    public void testDeleteTask() {
        // Insert a province into the database and get the ID
        int id = DatabaseInsertUtil.insertProvince("Provinces to Delete", "DE");

        try {
            // Send the deletion request and log the details
            given()
                .spec(TestUtil.addTokenHeader(RestAssured.given()))
                .when()
                .delete(RestAssured.baseURI + PROVINCES_PATH + "/" + id)
                .then()
                .statusCode(204);
        } finally {
            // Ensure the province is deleted even if the test fails
            DatabaseInsertUtil.deleteProvince(id);
        }
    }

    /**
     * Test case to filter provinces by name with pagination via API.
     * It validates if the filtered provinces match the expected criteria.
     */
    @Test
    public void testFilterNameWithPaginationTask() {
        int id = DatabaseInsertUtil.insertProvince("Province Filter Name with Pagination", "PG");

        try {
            given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
                .queryParam("name", "Province Filter Name with Pagination")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get(RestAssured.baseURI + PROVINCES_PATH)
                .then()
                .statusCode(200)
                .body("_embedded.provinces[0].id", equalTo(id))
                .body("_embedded.provinces[0].name", equalTo("Province Filter Name with Pagination"))
                .body("_embedded.provinces[0].abbreviation", equalTo("PG"));
        } finally {
            DatabaseInsertUtil.deleteProvince(id);
        }
    }
    
    /**
     * Test case to filter provinces by abbreviation with pagination via API.
     * It validates if the filtered provinces match the expected criteria.
     */
    @Test
    public void testFilterAbbreviationWithPaginationTask() {
        int id = DatabaseInsertUtil.insertProvince("Province Filter Abbreviation with Pagination", "AA");

        try {
            given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
                .queryParam("abbreviation", "AA")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get(RestAssured.baseURI + PROVINCES_PATH)
                .then()
                .statusCode(200)
                .body("_embedded.provinces[0].id", equalTo(id))
                .body("_embedded.provinces[0].name", equalTo("Province Filter Abbreviation with Pagination"))
                .body("_embedded.provinces[0].abbreviation", equalTo("AA"));
        } finally {
            DatabaseInsertUtil.deleteProvince(id);
        }
    }
    
    /**
     * Test case to filter provinces by both name and abbreviation with pagination via API.
     * It validates if the filtered provinces match the expected criteria.
     */
    @Test
    public void testFilterNameAndAbbreviationWithPaginationTask() {
        int id = DatabaseInsertUtil.insertProvince("Province Filter Name And Abbreviation with Pagination", "NA");

        try {
            given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
                .queryParam("name", "Province Filter Name And Abbreviation with Pagination")
                .queryParam("abbreviation", "NA")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get(RestAssured.baseURI + PROVINCES_PATH)
                .then()
                .statusCode(200)
                .body("_embedded.provinces[0].id", equalTo(id))
                .body("_embedded.provinces[0].name", equalTo("Province Filter Name And Abbreviation with Pagination"))
                .body("_embedded.provinces[0].abbreviation", equalTo("NA"));
        } finally {
            DatabaseInsertUtil.deleteProvince(id);
        }
    }
    
    /**
     * Test case to filter provinces by name without pagination via API.
     * It validates if the filtered provinces match the expected criteria.
     */
    @Test
    public void testFilterProvincesByNameNoPagination() {
        int id = DatabaseInsertUtil.insertProvince("Province Filter Name And no Pagination", "NO");

        try {
            given()
                .spec(TestUtil.addTokenHeader(RestAssured.given())) // Uses the helper method to add the authorization header
                .queryParam("name", "Province Filter Name And no Pagination")
                .when()
                .get(RestAssured.baseURI + PROVINCES_PATH + PROVINCES_PATH_NOPAGINATION)
                .then()
                .statusCode(200)
                .body("_embedded.provinces[0].name", equalTo("Province Filter Name And no Pagination"))
                .body("_embedded.provinces[0].abbreviation", equalTo("NO")) // Checks the description
                .body("page", nullValue()); // Checks if the "page" key is not present in the JSON response
            
		} finally {
			DatabaseInsertUtil.deleteProvince(id);
		}
    }
    
    /**
     * Test case to create a province with maximum character length for name via API.
     * It validates if the API correctly handles the maximum length constraint for name.
     */
    @Test
    public void testCreateNameMaxCaractereProvince() {
    	String textName = "The project's goal is academic and aims to demonstrate knowledge of software quality, with an emphasis on this aspect ";
    	String requestBody = "{ \"name\": \"" + textName + "\", \"abbreviation\": \"VV\"}";

    	 given()
         .spec(TestUtil.addTokenHeader(RestAssured.given()))
         .body(requestBody)
         .when()
         .post(RestAssured.baseURI + PROVINCES_PATH)
         .then()
         .statusCode(400)
         .body("status", equalTo(400))
         .body("timestamp", notNullValue())
         .body("type", equalTo("http://localhost:8080/max-length"))
         .body("title", equalTo("Maximum length exceeded"))
         .body("detail", containsString("Data too long for column 'name'"))
         .body("userMessage", equalTo("An unexpected internal system error has occurred. Please try again and if the problem persists, contact your system administrator"));
    }
    
    /**
     * Test case to create a province with maximum character length for abbreviation via API.
     * It validates if the API correctly handles the maximum length constraint for abbreviation.
     */
    @Test
    public void testCreateAbbreviatio9nMaxCaractereProvince() {
    	String textName = "The project's goal  ";
    	String requestBody = "{ \"name\": \"" + textName + "\", \"abbreviation\": \"ALMLUPI\"}";

    	 given()
         .spec(TestUtil.addTokenHeader(RestAssured.given()))
         .body(requestBody)
         .when()
         .post(RestAssured.baseURI + PROVINCES_PATH)
         .then()
         .statusCode(400)
         .body("status", equalTo(400))
         .body("timestamp", notNullValue())
         .body("type", equalTo("http://localhost:8080/max-length"))
         .body("title", equalTo("Maximum length exceeded"))
         .body("detail", containsString("Data too long for column 'abbreviation'"))
         .body("userMessage", equalTo("An unexpected internal system error has occurred. Please try again and if the problem persists, contact your system administrator"));
    }
}
