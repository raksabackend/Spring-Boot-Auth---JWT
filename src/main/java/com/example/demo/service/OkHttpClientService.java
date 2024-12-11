package com.example.demo.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class OkHttpClientService {

    private final OkHttpClient okHttpClient;
    private final EurekaClient eurekaClient;

    @Autowired
    public OkHttpClientService(OkHttpClient.Builder okHttpClientBuilder, EurekaClient eurekaClient) {
        this.okHttpClient  = okHttpClientBuilder.build();
        this.eurekaClient = eurekaClient;
    }

    public String getExample() throws IOException {
        // Build Request
        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();

        // Execute Request, Return Body if Request Successful
        try(Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            assert response.body() != null;
            return response.body().string();
        }

    }

    public String callToOtherService() throws IOException {

        // Discover the instance from Eureka
        InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("client1-orderservice", false);

        // Get the url of the service
        String serviceUrl = instanceInfo.getHomePageUrl();

        System.out.println("hahahah" + serviceUrl);

        // Create a request to the discovered service
        Request request = new Request.Builder()
                .url(serviceUrl + "order/sayHi")
                .build();

        try(Response response = okHttpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            assert response.body() != null;
            return response.body().string();

        }

    }

    public String uploadFileToOtherService(MultipartFile file) throws IOException {

        InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("client1-orderservice", false);

        // Get the url of the service
        String serviceUrl = instanceInfo.getHomePageUrl();
        String uploadUrl = serviceUrl + "order/upload";

        System.out.println("testing" + uploadUrl);

        // Create A Request Body for the file
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getOriginalFilename(),
                        RequestBody.create(file.getBytes(),
                        MediaType.parse(file.getContentType())
                                )).build();

        // Create A Request to the discovered service
        Request request = new Request.Builder()
                .url(uploadUrl)
                .post(requestBody).build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            assert response.body() != null;
            return response.body().string();
        }

    }

}

