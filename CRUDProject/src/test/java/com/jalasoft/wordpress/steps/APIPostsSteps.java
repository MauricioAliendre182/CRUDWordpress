package com.jalasoft.wordpress.steps;

import api.APIConfig;
import api.APIManager;
import api.controller.APIController;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIPostsSteps {
    private static final APIConfig apiConfig = APIConfig.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private APIController controller = APIController.getInstance();
    private final String postsEndpoint = apiConfig.getPostsEndpoint();

    private final String postByIdEndpoint = apiConfig.getPostsByIdEndpoint();
    private Map<String, Object> params;


    @Given("^(?:I make|the user makes) a request to retrieve all users$")
    public void getAllUsers() {
        Header authHeader = controller.getHeader("Authorization");
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("per_page", 100);

        Response requestResponse = apiManager.get(postsEndpoint, queryParams, authHeader);
        controller.setResponse(requestResponse);
    }

    @Given("^I make a request to create a user with the following params$")
    public void createAUser(DataTable table) {
        List<Map<String, Object>> queryParamsList = table.asMaps(String.class, Object.class);
        Map<String, Object> queryParams = queryParamsList.get(0);

        Response requestResponse = apiManager.post(postsEndpoint, queryParams, controller.getHeader("Authorization"));
        controller.setResponse(requestResponse);
        controller.setId(requestResponse.jsonPath().getString("id"));
        params = queryParams;
    }

    @Given("^I make a request to retrieve a user$")
    public void getUserById() {
        Header authHeader = controller.getHeader("Authorization");
        Response requestResponse = apiManager.get(postByIdEndpoint.replace("<id>", controller.getId()), authHeader);

        Map<String, Object> queryParams = new HashMap<>();
        String name = controller.getResponse().jsonPath().getString("name");

        queryParams.put("id", controller.getId());
        queryParams.put("name", name);
        params = queryParams;
        controller.setResponse(requestResponse);
    }

    @Given("^I make a request to update a user with the following params$")
    public void updateUserById(DataTable table) {
        Header authHeader = controller.getHeader("Authorization");
        List<Map<String, Object>> queryParamsList = table.asMaps(String.class, Object.class);
        Map<String, Object> queryParams = queryParamsList.get(0);
        Response requestResponse = apiManager.put(postByIdEndpoint.replace("<id>", controller.getId()), queryParams, authHeader);

        Map<String, Object> queryParamsUpdate = new HashMap<>();
        String name = controller.getResponse().jsonPath().getString("name");
        String email = controller.getResponse().jsonPath().getString("email");

        queryParamsUpdate.put("id", controller.getId());
        queryParamsUpdate.put("name", name);
        queryParamsUpdate.put("email", email);

        params = queryParamsUpdate;
        controller.setResponse(requestResponse);
    }

    @Given("^I make a request to delete a user$")
    public void deleteUserById() {
        Header authHeader = controller.getHeader("Authorization");
        Map<String, Object> queryParamsDelete = new HashMap<>();
        queryParamsDelete.put("force", true);
        queryParamsDelete.put("reassign", 1);
        Response requestResponse = apiManager.delete(postByIdEndpoint.replace("<id>", controller.getId()), queryParamsDelete,authHeader);
        Map<String, Object> queryParams = new HashMap<>();

        String username = controller.getResponse().jsonPath().getString("username");
        String email = controller.getResponse().jsonPath().getString("email");

        queryParams.put("id", controller.getId());
        queryParams.put("username", username);
        queryParams.put("email", email);
        queryParams.put("deleted", true);

        params = queryParams;
        controller.setResponse(requestResponse);
    }

    @Then("^response should have a proper amount of users$")
    public void verifyUsersAmount() {
        int expectedAmountOfPosts = Integer.parseInt(controller.getResponse().getHeaders().getValue("X-WP-Total"));
        int actualAmountOfPosts = controller.getResponse().jsonPath().getList("$").size();
        Assert.assertEquals(actualAmountOfPosts, expectedAmountOfPosts, "wrong amount of posts returned");
    }

    @Then("^user should have been created with the proper params$")
    public void verifyCreatedUser() {
        String expectedUsername = (String) params.get("username");
        String expectedEmail = (String) params.get("email");

        Assert.assertTrue(controller.getResponse().jsonPath().getString("username").contains(expectedUsername), "wrong username value returned");
        Assert.assertTrue(controller.getResponse().jsonPath().getString("email").contains(expectedEmail), "wrong email value returned");
    }

    @Then("^user should have been retrieved with the proper params$")
    public void verifyRetrievedUser() {
        String expectedId = (String) params.get("id");
        String expectedName = (String) params.get("name");

        Assert.assertTrue(controller.getResponse().jsonPath().getString("id").contains(expectedId), "wrong id value returned");
        Assert.assertTrue(controller.getResponse().jsonPath().getString("name").contains(expectedName), "wrong username value returned");
    }

    @Then("^user should have been updated with the proper params$")
    public void verifyUpdatedUser() {
        String expectedId = (String) params.get("id");
        String expectedName = (String) params.get("name");
        String expectedEmail = (String) params.get("email");

        Assert.assertTrue(controller.getResponse().jsonPath().getString("id").contains(expectedId), "wrong id value returned");
        Assert.assertTrue(controller.getResponse().jsonPath().getString("name").contains(expectedName), "wrong username value returned");
        Assert.assertFalse(controller.getResponse().jsonPath().getString("email").contains(expectedEmail), "wrong email value returned");
    }

    @Then("^user should have been deleted$")
    public void verifyDeletedUser() {
        String expectedId = (String) params.get("id");
        String expectedUsername = (String) params.get("username");
        String expectedEmail = (String) params.get("email");

        Assert.assertTrue(controller.getResponse().jsonPath().getBoolean("deleted"), "wrong status");
        Assert.assertTrue(controller.getResponse().jsonPath().getString("previous.id").contains(expectedId), "wrong id value returned");
        Assert.assertTrue(controller.getResponse().jsonPath().getString("previous.username").contains(expectedUsername), "wrong username value returned");
        Assert.assertTrue(controller.getResponse().jsonPath().getString("previous.email").contains(expectedEmail), "wrong email value returned");
    }
}
