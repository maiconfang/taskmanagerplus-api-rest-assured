package com.maif.taskmanagerplus_api_rest_azure.util;


import com.maif.taskmanagerplus_api_rest_azure.auth.AuthUtil;

import io.restassured.specification.RequestSpecification;

public class TestUtil {

    public static RequestSpecification addTokenHeader(RequestSpecification requestSpec) {
        String authToken = AuthUtil.getAuthToken();
        return requestSpec
            .header("Authorization", "Bearer " + authToken)
            .header("Content-Type", "application/json");
    }
}
