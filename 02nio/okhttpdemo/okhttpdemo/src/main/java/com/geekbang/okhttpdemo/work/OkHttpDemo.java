package com.geekbang.okhttpdemo.work;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OkHttpDemo {
    final OkHttpClient client = new OkHttpClient();

    String getUrl(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


    public static void main(String[] args) throws IOException {
        OkHttpDemo okHttpDemo = new OkHttpDemo();
        String response = okHttpDemo.getUrl("http://localhost:8801");
        System.out.println(response);
    }
}
