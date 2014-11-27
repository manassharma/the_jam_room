package com.example.lenovo.thejamroom.async;

import android.os.AsyncTask;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;


import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by avaneeshdesai on 11/25/14.
 */
public class uploadFile extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... strings) {

        String url = strings[1];
        String fileUri = strings[0];
        HttpClient client = new DefaultHttpClient();
        try {
            HttpPost post = new HttpPost(url);
           // post.setHeader("Content-Type", "multipart/mixed");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            File file = new File(fileUri);
            FileBody fb = new FileBody(file);

            builder.addBinaryBody("file", file);
            builder.addTextBody("name","test1.mp3");
            HttpEntity entity = builder.build();

            post.setEntity(entity);
            //HttpResponse response = client.execute(post);

            System.out.println("Requesting : " + post.getRequestLine());
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = client.execute(post, responseHandler);

            System.out.println("responseBody : " + responseBody);


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            client.getConnectionManager().shutdown();
        }
        return null;
    }


}
