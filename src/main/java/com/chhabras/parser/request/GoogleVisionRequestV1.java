package com.chhabras.parser.request;

/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.chhabras.parser.Parser;
import com.chhabras.parser.impl.BonusParser;
import com.chhabras.parser.impl.DmParser;
import com.chhabras.parser.impl.EdekaParser;
import com.chhabras.parser.impl.KauflandParser;
import com.chhabras.parser.impl.LidlParser;
import com.chhabras.parser.impl.ReweParser;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobListOption;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.AnnotateFileResponse.Builder;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;
import com.google.protobuf.util.JsonFormat;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GoogleVisionRequestV1 {
    /**
     * Performs document text detection on a local image file.
     *
     * @param filePath The path to the local file to detect document text on.
     * @throws Exception on errors while closing the client.
     * @throws IOException on Input/Output errors.
     */
    // [START vision_fulltext_detection]
    TreeMap<Integer, Set<WordV1>> lineMap = new TreeMap<>();

    /**
     * Performs document text detection on a remote image on Google Cloud Storage.
     *
     * @param gcsPath The path to the remote file on Google Cloud Storage to detect document text on.
     * @throws Exception   on errors while closing the client.
     * @throws IOException on Input/Output errors.
     */
    // [START vision_fulltext_detection_gcs]
    public static void detectDocumentTextGcs(String gcsPath) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(gcsPath).build();
        Image img = Image.newBuilder().setSource(imgSource).build();
        Feature feat = Feature.newBuilder().setType(Type.DOCUMENT_TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            client.close();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return;
                }
                // For full list of available annotations, see http://g.co/cloud/vision/docs
                TextAnnotation annotation = res.getFullTextAnnotation();
                for (Page page : annotation.getPagesList()) {
                    String pageText = "";
                    for (Block block : page.getBlocksList()) {
                        String blockText = "";
                        for (Paragraph para : block.getParagraphsList()) {
                            String paraText = "";
                            for (Word word : para.getWordsList()) {
                                String wordText = "";
                                for (Symbol symbol : word.getSymbolsList()) {
                                    wordText = wordText + symbol.getText();
                                    System.out.format(
                                            "Symbol text: %s (confidence: %f)%n",
                                            symbol.getText(), symbol.getConfidence());
                                }
                                System.out.format(
                                        "WordV1 text: %s (confidence: %f)%n%n", wordText, word.getConfidence());
                                paraText = String.format("%s %s", paraText, wordText);
                            }
                            // Output Example using Paragraph:
                            System.out.println("%nParagraph: %n" + paraText);
                            System.out.format("Paragraph Confidence: %f%n", para.getConfidence());
                            blockText = blockText + paraText;
                        }
                        pageText = pageText + blockText;
                    }
                }
                System.out.println("%nComplete annotation:");
                System.out.println(annotation.getText());
            }
        }
    }

    /**
     * Performs document text OCR with PDF/TIFF as source files on Google Cloud Storage.
     *
     * @param gcsSourcePath      The path to the remote file on Google Cloud Storage to detect document
     *                           text on.
     * @param gcsDestinationPath The path to the remote file on Google Cloud Storage to store the
     *                           results on.
     * @throws Exception on errors while closing the client.
     */
    public static void detectDocumentsGcs(String gcsSourcePath, String gcsDestinationPath)
            throws Exception {

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            List<AsyncAnnotateFileRequest> requests = new ArrayList<>();

            // Set the GCS source path for the remote file.
            GcsSource gcsSource = GcsSource.newBuilder().setUri(gcsSourcePath).build();

            // Create the configuration with the specified MIME (Multipurpose Internet Mail Extensions)
            // types
            InputConfig inputConfig =
                    InputConfig.newBuilder()
                            .setMimeType(
                                    "application/pdf") // Supported MimeTypes: "application/pdf", "image/tiff"
                            .setGcsSource(gcsSource)
                            .build();

            // Set the GCS destination path for where to save the results.
            GcsDestination gcsDestination =
                    GcsDestination.newBuilder().setUri(gcsDestinationPath).build();

            // Create the configuration for the System.output with the batch size.
            // The batch size sets how many pages should be grouped into each json System.output file.
            OutputConfig outputConfig =
                    OutputConfig.newBuilder().setBatchSize(2).setGcsDestination(gcsDestination).build();

            // Select the Feature required by the vision API
            Feature feature = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();

            // Build the OCR request
            AsyncAnnotateFileRequest request =
                    AsyncAnnotateFileRequest.newBuilder()
                            .addFeatures(feature)
                            .setInputConfig(inputConfig)
                            .setOutputConfig(outputConfig)
                            .build();

            requests.add(request);

            // Perform the OCR request
            OperationFuture<AsyncBatchAnnotateFilesResponse, OperationMetadata> response =
                    client.asyncBatchAnnotateFilesAsync(requests);

            System.out.println("Waiting for the operation to finish.");

            // Wait for the request to finish. (The result is not used, since the API saves the result to
            // the specified location on GCS.)
            List<AsyncAnnotateFileResponse> result =
                    response.get(180, TimeUnit.SECONDS).getResponsesList();

            // Once the request has completed and the System.output has been
            // written to GCS, we can list all the System.output files.
            Storage storage = StorageOptions.getDefaultInstance().getService();

            // Get the destination location from the gcsDestinationPath
            Pattern pattern = Pattern.compile("gs://([^/]+)/(.+)");
            Matcher matcher = pattern.matcher(gcsDestinationPath);

            if (matcher.find()) {
                String bucketName = matcher.group(1);
                String prefix = matcher.group(2);

                // Get the list of objects with the given prefix from the GCS bucket
                Bucket bucket = storage.get(bucketName);
                com.google.api.gax.paging.Page<Blob> pageList = bucket.list(BlobListOption.prefix(prefix));

                Blob firstOutputFile = null;

                // List objects with the given prefix.
                System.out.println("Output files:");
                for (Blob blob : pageList.iterateAll()) {
                    System.out.println(blob.getName());

                    // Process the first System.output file from GCS.
                    // Since we specified batch size = 2, the first response contains
                    // the first two pages of the input file.
                    if (firstOutputFile == null) {
                        firstOutputFile = blob;
                    }
                }

                // Get the contents of the file and convert the JSON contents to an AnnotateFileResponse
                // object. If the Blob is small read all its content in one request
                // (Note: the file is a .json file)
                // Storage guide: https://cloud.google.com/storage/docs/downloading-objects
                String jsonContents = new String(firstOutputFile.getContent());
                Builder builder = AnnotateFileResponse.newBuilder();
                JsonFormat.parser().merge(jsonContents, builder);

                // Build the AnnotateFileResponse object
                AnnotateFileResponse annotateFileResponse = builder.build();

                // Parse through the object to get the actual response for the first page of the input file.
                AnnotateImageResponse annotateImageResponse = annotateFileResponse.getResponses(0);

                // Here we print the full text from the first page.
                // The response contains more information:
                // annotation/pages/blocks/paragraphs/words/symbols
                // including confidence score and bounding boxes
                System.out.format("%nText: %s%n", annotateImageResponse.getFullTextAnnotation().getText());
            } else {
                System.out.println("No MATCH");
            }
        }
    }

    /**
     * Detects localized objects in the specified local image.
     *
     * @param filePath The path to the file to perform localized object detection on.
     * @throws Exception   on errors while closing the client.
     * @throws IOException on Input/Output errors.
     */
    public static void detectLocalizedObjects(String filePath) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder()
                        .addFeatures(Feature.newBuilder().setType(Type.OBJECT_LOCALIZATION))
                        .setImage(img)
                        .build();
        requests.add(request);

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            // Perform the request
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            // Display the results
            for (AnnotateImageResponse res : responses) {
                for (LocalizedObjectAnnotation entity : res.getLocalizedObjectAnnotationsList()) {
                    System.out.format("Object name: %s%n", entity.getName());
                    System.out.format("Confidence: %s%n", entity.getScore());
                    System.out.format("Normalized Vertices:%n");
                    entity
                            .getBoundingPoly()
                            .getNormalizedVerticesList()
                            .forEach(vertex -> System.out.format("- (%s, %s)%n", vertex.getX(), vertex.getY()));
                }
            }
        }
    }

    /**
     * Detects localized objects in a remote image on Google Cloud Storage.
     *
     * @param gcsPath The path to the remote file on Google Cloud Storage to detect localized objects
     *                on.
     * @throws Exception   on errors while closing the client.
     * @throws IOException on Input/Output errors.
     */
    public static void detectLocalizedObjectsGcs(String gcsPath) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(gcsPath).build();
        Image img = Image.newBuilder().setSource(imgSource).build();

        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder()
                        .addFeatures(Feature.newBuilder().setType(Type.OBJECT_LOCALIZATION))
                        .setImage(img)
                        .build();
        requests.add(request);

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            // Perform the request
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            client.close();
            // Display the results
            for (AnnotateImageResponse res : responses) {
                for (LocalizedObjectAnnotation entity : res.getLocalizedObjectAnnotationsList()) {
                    System.out.format("Object name: %s%n", entity.getName());
                    System.out.format("Confidence: %s%n", entity.getScore());
                    System.out.format("Normalized Vertices:%n");
                    entity
                            .getBoundingPoly()
                            .getNormalizedVerticesList()
                            .forEach(vertex -> System.out.format("- (%s, %s)%n", vertex.getX(), vertex.getY()));
                }
            }
        }
    }

    public List<String> detectDocumentText(String file) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();
        String text = "";
        ByteString imgBytes = ByteString.readFrom(GoogleVisionRequestV1.class.getClassLoader().getResourceAsStream("images/" + file));
        Image img = Image.newBuilder().setContent(imgBytes).build();

        Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);


        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            client.close();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());

                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                TextAnnotation annotation = res.getFullTextAnnotation();
                for (Page page : annotation.getPagesList()) {

                    String pageText = "";
                    int x_axis = -1;
                    int y_axis = -1;
                    for (Block block : page.getBlocksList()) {
                        String blockText = "";
                        for (Paragraph para : block.getParagraphsList()) {
                            String paraText = "";
                            for (Word word : para.getWordsList()) {
                                String wordText = "";

                                for (Symbol symbol : word.getSymbolsList()) {
                                    wordText = wordText + symbol.getText();
                                    /*System.out.format(
                                            "Symbol text: %s (confidence: %f)%n",
                                            symbol.getText(), symbol.getConfidence());

                                         */
                                }
                                /* System.out.format(
                                        "WordV1 text: %s (confidence: %f)%n%n", wordText, word.getConfidence());*/
                                //System.out.println(wordText);
                                x_axis = word.getBoundingBox().getVertices(0).getX();
                                y_axis = word.getBoundingBox().getVertices(0).getY();
                                Coordinates coordinates = new Coordinates(x_axis, y_axis);
                                operateLine(wordText, coordinates);
                                paraText = String.format("%s %s", paraText, wordText);
                            }
                            // Output Example using Paragraph:
                            // System.out.println("%nParagraph: %n" + paraText);
                            // System.out.format("Paragraph Confidence: %f%n", para.getConfidence());
                            blockText = blockText + paraText;
                        }
                        pageText = pageText + blockText;
                    }
                }
                //text = annotation.getText();
                //  System.out.println("%nComplete annotation:");
                // System.out.println(text);
            }
        }
        List<String> results = new ArrayList();
        List<String> results2 = new ArrayList();

        lineMap.values().stream().forEach(line -> {
            StringBuilder builder = new StringBuilder();
            line.stream().forEach(word -> builder.append(word).append(" "));
            results.add(builder.toString());
        });
        //results = lineMap.values().stream().map(line -> line.stream()).flatMap(word -> word.map(w->w.getText()).collect(Collectors.toList());
        results2 = lineMap.values()
                .stream()
                .flatMap(list -> list.stream())
                .map(word -> word.getText())
                .collect(Collectors.toList());
        printMap(lineMap);
        return results;
        //return results2;

    }

    public Parser decider(List<String> list) throws Exception {
        for (String text : list) {
            Parser p = getParser(text);
            if (p != null) {
                return p;
            }
        }
        return null;
    }

    private Parser getParser(String text) {
        if (text.toLowerCase().contains("lidl") || text.toLowerCase().contains("frauenhoferstr. 2")|| text.toLowerCase().contains("ldl")) {
            System.out.println("######## lidl Parser ########");
            return new LidlParser();
        } else if (text.toLowerCase().contains("edeka")) {
            System.out.println("######## edeka Parser ########");
            return new EdekaParser();
        } else if (text.toLowerCase().contains("rewe")) {
            System.out.println("######## rewe Parser ########");
            return new ReweParser();
        } else if (text.toLowerCase().contains("drogerie") || text.toLowerCase().contains("drogerle")) {
            System.out.println("######## dm Parser ########");
            return new DmParser();
        } else if (text.toLowerCase().contains("kaufland")) {
            System.out.println("######## kaufland Parser ########");
            return new KauflandParser();
        } else if (text.toLowerCase().contains("bonus")) {
            System.out.println("######## Bonus Parser ########");
            return new BonusParser();
        }
        return null;
    }

    public void printMap(Map mp) {
        System.out.println("#######################################");
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            System.out.println(pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        System.out.println("#######################################");
    }

    private void operateLine(String wordText, Coordinates coordinates) {
        int index = considerNewOrExistingLine(coordinates.getY());
        WordV1 w = new WordV1(wordText, coordinates.getX(), coordinates.getY());
        if (index > 0) {
            lineMap.get(index).add(w);
        } else {
            Set<WordV1> words = new TreeSet<>();
            words.add(w);
            lineMap.put(coordinates.getY(), words);
        }
    }


    private int considerNewOrExistingLine(int y_axis) {
        Iterator it = lineMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if ((y_axis < Integer.parseInt(pair.getKey().toString()) + 20)) {
                return Integer.parseInt(pair.getKey().toString());
            }
        }
        return -1;
    }
}
