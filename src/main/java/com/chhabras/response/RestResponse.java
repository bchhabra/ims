package com.chhabras.response;

public class RestResponse {
    private String responseBody;
    private int statusCode;

    public RestResponse(String responseBody, int statusCode) {
        this.responseBody = responseBody;
        this.statusCode = statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        return "RestResponse{" +
                "responseBody='" + responseBody + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}

