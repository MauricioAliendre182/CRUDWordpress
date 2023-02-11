package api.controller;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import utils.LoggerManager;


import java.util.ArrayList;
import java.util.List;

public class APIController {
    private final List<Header> headers = new ArrayList<>();
    private Response response;
    private String id;
    private static APIController instance;

    public static APIController getInstance() {
        if (instance == null) {
            instance = new APIController();
        }
        return instance;
    }

    public void addHeader(Header header) {
        headers.add(header);
    }

    public Header getHeader(String headerName) {
        return new Headers(headers).get(headerName);
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
