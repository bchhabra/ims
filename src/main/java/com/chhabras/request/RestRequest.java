package com.chhabras.request;

import io.restassured.specification.MultiPartSpecification;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Map;

public interface RestRequest {
    String getUrl();
    Map<String, Object> getHeaders();
    Map<String, Object> getFormData() throws URISyntaxException;
}
