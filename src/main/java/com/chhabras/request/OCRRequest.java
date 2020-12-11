package com.chhabras.request;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class OCRRequest implements RestRequest {

    String file;

    public OCRRequest(String file) {
        this.file = file;
    }

    @Override
    public String getUrl() {
        return "image";
    }

    @Override
    public Map<String, Object> getHeaders() {
        Map<String, Object> map = new HashMap<>();
        map.put("apikey", "6cba923fe288957");
        return map;
    }


    private File getFile() throws URISyntaxException {
        return new File(OCRRequest.class.getClassLoader().getResource("images/"+file).toURI());
    }

    @Override
    public Map<String, Object> getFormData() throws URISyntaxException {
        Map<String, Object> map = new HashMap<>();
        map.put("language", "eng");
        map.put("isOverlayRequired", "false");
        map.put("iscreatesearchablepdf", "false");
        map.put("issearchablepdfhidetextlayer", "false");
        map.put("file", getFile());
        return map;
    }

}