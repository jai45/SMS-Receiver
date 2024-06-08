package com.example.myapplication;


import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class HttpUtils extends AsyncTask<String, Void, String> {
    private final String url;
    private final Map<String, String> params;

    private final TaskListener listener;


    public HttpUtils(String url, Map<String, String> params, TaskListener listener) {
        this.url = url;
        this.params = params;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        URL Url = null;

        String responseMessage = "";
        try {
            Log.d("d","hello");
            Url = new URL(url);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) Url.openConnection();
            httpsURLConnection.setRequestMethod("POST");

            httpsURLConnection.setDoOutput(true);
            OutputStream outputStream = httpsURLConnection.getOutputStream();
            Gson gson = new Gson();
            String requestBody = gson.toJson(this.params);
            outputStream.write(requestBody.getBytes());
            outputStream.flush();
            outputStream.close();


            InputStream inputStream = httpsURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            responseMessage = responseBuilder.toString();
        }
        catch (Exception e){
            Log.d("2", String.valueOf(e.getStackTrace()));
            e.printStackTrace();
        }
        return responseMessage;
    }

    @Override
    protected void onPostExecute(String stringStringMap) {
        super.onPostExecute(stringStringMap);
        listener.onTaskComplete(stringStringMap);
    }
}
