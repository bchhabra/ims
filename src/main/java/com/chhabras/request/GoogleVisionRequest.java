package com.chhabras.request;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageContext;
import com.google.protobuf.ByteString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GoogleVisionRequest {
    String file;
    public GoogleVisionRequest(String file) {
        this.file = file;
    }

    public String detectText() throws Exception {
        List<AnnotateImageRequest> requests = new ArrayList<>();
        ByteString imgBytes = ByteString.readFrom(GoogleVisionRequest.class.getClassLoader().getResourceAsStream("images/"+file));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().mergeImageContext(ImageContext.newBuilder().addLanguageHints("de").build()).addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            Optional<AnnotateImageResponse> first = responses.stream().findFirst();
            AnnotateImageResponse response1 =  first.get();
            EntityAnnotation annotation = response1.getTextAnnotationsList().stream().findFirst().get();
            return annotation.getDescription();
            //printAllResponses(responses);
        }
    }

    private void printAllResponses(List<AnnotateImageResponse> responses) {
        for (AnnotateImageResponse res : responses) {
            if (res.hasError()) {
                System.out.println("Error: %s\n" +res.getError().getMessage());
                return;
            }
            // For full list of available annotations, see http://g.co/cloud/vision/docs
            for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                System.out.println("Text: %s\n"+ annotation.getDescription());
                System.out.println("Position : %s\n"+ annotation.getBoundingPoly());
            }
        }
    }
}
