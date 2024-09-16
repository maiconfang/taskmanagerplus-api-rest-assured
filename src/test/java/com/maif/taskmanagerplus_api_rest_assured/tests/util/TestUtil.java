package com.maif.taskmanagerplus_api_rest_assured.tests.util;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    
    /**
     * Converts a date string in "yyyy-MM-dd" format to LocalDate.
     *
     * @param dateString The date string in "yyyy-MM-dd" format.
     * @return LocalDate representing the input date.
     */
    public static LocalDate convertToLocalDate(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    /**
     * Converts an object to its JSON string representation.
     * This method uses Jackson's ObjectMapper to serialize the given object into a JSON string.
     * If an exception occurs during serialization, it throws a RuntimeException.
     *
     * @param object The object to be converted to JSON.
     * @return The JSON string representation of the object.
     * @throws RuntimeException If there is a failure during JSON processing.
     */
    public static String convertObjectToJson(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }


    
}
