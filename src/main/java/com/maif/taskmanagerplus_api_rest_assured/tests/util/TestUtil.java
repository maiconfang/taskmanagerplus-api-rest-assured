package com.maif.taskmanagerplus_api_rest_assured.tests.util;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.maif.taskmanagerplus_api_rest_assured.auth.AuthUtil;

import io.restassured.specification.RequestSpecification;

/**
 * Adds authorization token and Content-Type header to the given RequestSpecification.
 * The authorization token is fetched using AuthUtil.getAuthToken().
 *
 * @param requestSpec The RequestSpecification to which headers will be added.
 * @return The updated RequestSpecification with Authorization and Content-Type headers.
 * 
 * Author: Maicon Fang
 * Date: 2024-06-18
 * 
 */
public class TestUtil {

    public static RequestSpecification addTokenHeader(RequestSpecification requestSpec) {
        String authToken = AuthUtil.getAuthToken();
        return requestSpec
            .header("Authorization", "Bearer " + authToken)
            .header("Content-Type", "application/json");
    }
    
    /**
     * Converts a date string in "yyyy-MM-dd" format to LocalDate.
     *
     * @param dateString The date string in "yyyy-MM-dd" format.
     * @return LocalDate representing the input date.
     */
    public static LocalDate convertToLocalDate(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
