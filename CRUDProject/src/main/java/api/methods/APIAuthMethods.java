package api.methods;

import api.APIConfig;
import api.APIManager;
import framework.CredentialsManager;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.internal.http.Status;
import io.restassured.response.Response;
import utils.LoggerManager;

import java.util.HashMap;
import java.util.Map;

public class APIAuthMethods {
    private static final LoggerManager log = LoggerManager.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private static final CredentialsManager credentialsManager = CredentialsManager.getInstance();
    private static final APIConfig apiConfig = APIConfig.getInstance();

    public static Header getAuthHeader(String userRole) {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("username", credentialsManager.getUsername(userRole));
        jsonAsMap.put("password", credentialsManager.getPassword(userRole));
        String tokenEndpoint = apiConfig.getTokenEndpoint();
        Response response = apiManager.post(tokenEndpoint, ContentType.JSON, jsonAsMap);

        if (Status.SUCCESS.matches(response.getStatusCode()))
        {
            log.info("Authentication Token retrieved");
            String tokenType = response.jsonPath().getString("token_type");
            String token = response.jsonPath().getString("jwt_token");
            String authorization = tokenType + " " + token;
            return new Header("Authorization", authorization);
        } else {
            log.error("Failed to retrieve Authentication Token");
            return null;
        }
    }
}
