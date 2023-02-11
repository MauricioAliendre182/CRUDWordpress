package api.methods;

import api.APIConfig;
import api.APIManager;
import api.controller.APIController;
import io.restassured.http.Header;
import io.restassured.response.Response;
import utils.LoggerManager;

import java.util.HashMap;
import java.util.Map;

public class APIPostsMethods {
    public static final LoggerManager log = LoggerManager.getInstance();
    public static final APIManager apiManager = APIManager.getInstance();
    private static final APIConfig apiConfig = APIConfig.getInstance();
    private static APIController controller = APIController.getInstance();

    public static Response createAPost() {
        String postsEndpoint = apiConfig.getPostsEndpoint();
        Header authHeader = controller.getHeader("Authorization");
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("username", "Mark44");
        jsonAsMap.put("email", "Amperman@gmail.com");
        jsonAsMap.put("password", "123456");
        Response newUser = apiManager.post(postsEndpoint, jsonAsMap, authHeader);
        controller.setResponse(newUser);
        controller.setId(newUser.jsonPath().getString("id"));

        return controller.getResponse();
    }

    public static Response deleteAPostById(String id) {
        String postByIdEndpoint = apiConfig.getPostsByIdEndpoint().replace("<id>", id);
        Header authHeader = controller.getHeader("Authorization");
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("force", true);
        jsonAsMap.put("reassign", 1);

        return apiManager.delete(postByIdEndpoint, jsonAsMap, authHeader);
    }
}
