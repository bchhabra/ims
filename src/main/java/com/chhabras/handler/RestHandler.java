package com.chhabras.handler;

import com.chhabras.request.RestRequest;
import com.chhabras.response.RestResponse;

import java.net.URISyntaxException;

public interface RestHandler {
    RestResponse sendRequest(RestRequest request) throws URISyntaxException;
}
