package com.example.lenovo.thejamroom.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.lenovo.thejamroom.util.UserSession;

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

/**
 * Created by avaneeshdesai on 11/25/14.
 */
public class uploadFile extends AsyncTask<String, Void, String> {
    Context activity;
    ProgressDialog dialog;
    public uploadFile(Context context){
        activity = context;
    }

    protected void onPreExecute()
    {
        dialog = new ProgressDialog(activity);
        dialog.setMessage("Publishing your song...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {


        String fileUri = strings[0];
        String url = strings[1];
        String title = strings[2];
        String artist = strings[3];
        String fileName = strings[4];
        String genre = strings[5];
        String location = strings[6];
        String uploadedBy = strings[7];

        HttpClient client = new DefaultHttpClient();
        try {
            HttpPost post = new HttpPost(url);
            post.setHeader("x-auth-token", UserSession.getAccessToken());
           // post.setHeader("Content-Type", "multipart/mixed");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            File file = new File(fileUri);
            FileBody fb = new FileBody(file);

            builder.addBinaryBody("file", file);
            builder.addTextBody("name", fileName);
            builder.addTextBody("title", title);
            builder.addTextBody("artist",artist);
            builder.addTextBody("genre",genre);
            builder.addTextBody("location",location);
            builder.addTextBody("uploadedBy",uploadedBy);
            HttpEntity entity = builder.build();

            post.setEntity(entity);
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

    @Override
    protected void onPostExecute(final String success)
    {   super.onPostExecute(success);
        if (dialog.isShowing())
        {
            dialog.dismiss();
        }

    }


}
