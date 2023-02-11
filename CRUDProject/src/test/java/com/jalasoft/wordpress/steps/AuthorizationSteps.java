package com.jalasoft.wordpress.steps;

import api.controller.APIController;
import api.methods.APIAuthMethods;
import io.cucumber.java.en.Given;
import io.restassured.http.Header;
import org.testng.Assert;

public class AuthorizationSteps {
    private final APIController controller = APIController.getInstance();

    @Given("^(?:I am|the user is) authenticated with \"(.*?)\" role$")
    public void getToken(String userRole) {
        Header authHeader = APIAuthMethods.getAuthHeader(userRole);
        controller.addHeader(authHeader);
        Assert.assertNotNull(authHeader, "Unable to retrieve authorization header for user with role --> " + userRole);
    }
}
