package com.jalasoft.wordpress.steps.hooks.features;

import api.controller.APIController;
import api.methods.APIPostsMethods;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.internal.http.Status;
import io.restassured.response.Response;
import org.testng.Assert;

public class APIPostsFeatureHook {
    private final APIController controller = APIController.getInstance();

    @Before("@RetrieveAUser or @UpdateAUser or @DeleteAUser")
    public void createAPost() {
        Response requestResponse = APIPostsMethods.createAPost();
        controller.setResponse(requestResponse);

        Assert.assertTrue(Status.SUCCESS.matches(requestResponse.getStatusCode()), "post was not created");
    }

    @After("@CreateAUser or @RetrieveAUser or @UpdateAUser")
    public void deleteAPostById() {
        Response requestResponse = APIPostsMethods.deleteAPostById(controller.getId());

        Assert.assertTrue(Status.SUCCESS.matches(requestResponse.getStatusCode()), "post with id -> " + controller.getId() + " was not deleted");
    }
}
