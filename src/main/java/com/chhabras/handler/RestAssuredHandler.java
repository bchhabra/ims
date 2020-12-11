package com.chhabras.handler;

import com.chhabras.request.RestRequest;
import com.chhabras.response.RestResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class RestAssuredHandler implements RestHandler {

    private String username;
    private String password;
    private String baseurl;

    @Override
    public RestResponse sendRequest(RestRequest request) throws URISyntaxException {
        readConfiguration();
        String requestUrl = baseurl + request.getUrl();
        System.out.println(requestUrl);
        RequestSpecification resultingRequest = RestAssured.given();
        resultingRequest = resultingRequest
                .auth()
                .preemptive()
                .basic(username, password)
                .headers(request.getHeaders())
                .contentType("multipart/form-data");

        for (Map.Entry<String, Object> entry : request.getFormData().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            resultingRequest.multiPart(key,value);
        }

        Response response = resultingRequest.post(requestUrl);
        System.out.println(response.getBody());
        return convertResponse(response);
    }

    private void readConfiguration(){
        InputStream inputStream;
        try {
            Properties prop = new Properties();
            String propFileName = "configuration.properties";

            inputStream = RestAssuredHandler.class.getClassLoader().getResourceAsStream("conf/"+propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
                username = prop.getProperty("username");
                password = prop.getProperty("password");
                baseurl = prop.getProperty("baseurl");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private RestResponse convertResponse(Response response) {
        return new RestResponse(response.getBody().asString(), response.getStatusCode());
    }


}


