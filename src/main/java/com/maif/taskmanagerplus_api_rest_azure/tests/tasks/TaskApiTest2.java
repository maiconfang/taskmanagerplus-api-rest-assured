package com.maif.taskmanagerplus_api_rest_azure.tests.tasks;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import com.maif.taskmanagerplus_api_rest_azure.tests.base.BaseTest;

import io.restassured.RestAssured;

public class TaskApiTest2 extends BaseTest {
    
    @Test
    public void testHelloWorld01() {
        given()
            .when()
            .get(RestAssured.baseURI + "/tasks/hello")
            .then()
            .statusCode(200)
            .body(equalTo("Hello World!"));
    }
    
    @Test
    public void testHelloWorld02() {
        given()
            .when()
            .get(RestAssured.baseURI + "/tasks/hello")
            .then()
            .statusCode(200)
            .body(equalTo("Hello World!"));
    }

    
    @Test
    public void testHelloWorld03() {
        given()
            .when()
            .get(RestAssured.baseURI + "/tasks/hello")
            .then()
            .statusCode(200)
            .body(equalTo("Hello World!"));
    }

    
    @Test
    public void testHelloWorld04() {
        given()
            .when()
            .get(RestAssured.baseURI + "/tasks/hello")
            .then()
            .statusCode(200)
            .body(equalTo("Hello World!"));
    }

    
    @Test
    public void testHelloWorld05() {
        given()
            .when()
            .get(RestAssured.baseURI + "/tasks/hello")
            .then()
            .statusCode(200)
            .body(equalTo("Hello World!"));
    }

    
    @Test
    public void testHelloWorld06() {
        given()
            .when()
            .get(RestAssured.baseURI + "/tasks/hello")
            .then()
            .statusCode(200)
            .body(equalTo("Hello World!"));
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
