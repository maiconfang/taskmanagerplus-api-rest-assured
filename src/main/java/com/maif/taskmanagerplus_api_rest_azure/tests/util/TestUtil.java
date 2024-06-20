package com.maif.taskmanagerplus_api_rest_azure.tests.util;


import com.maif.taskmanagerplus_api_rest_azure.auth.AuthUtil;

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
}
