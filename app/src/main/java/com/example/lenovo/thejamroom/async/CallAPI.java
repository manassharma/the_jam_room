package com.example.lenovo.thejamroom.async;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Avaneesh on 22/10/2014.
 */
public class CallAPI extends AsyncTask <String, Void, String> {


    @Override
    protected String doInBackground(String... params) {
        String urlString = params[0]; // URL to call
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = "[]" ;
       // //= "[{\"songId\":2,\"name\":\"There She Goes\",\"artist\":\"Six Pence\",\"location\":\"US\",\"genere\":\"Pop\",\"uploadedOn\":\"2014-10-19\",\"path\":\"/resources/test.mp3\"},{\"songId\":3,\"name\":\"Summer of 69\",\"artist\":\"Adam \",\"location\":\"US\",\"genere\":\"Rock\",\"uploadedOn\":\"2014-10-15\",\"path\":\"/resources/test.mp3\"},{\"songId\":4,\"name\":\"Rude\",\"artist\":\"Magic\",\"location\":\"US\",\"genere\":\"Pop\",\"uploadedOn\":\"2014-10-15\",\"path\":\"/resources/test.mp3\"}]";
        try {
            response = httpclient.execute(new HttpGet(params[0]));
            Log.d("Response", Integer.toString(response.getStatusLine().getStatusCode()));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
                Log.d("Response Text", responseString);
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (Exception e) {
            //TODO Handle problems..
        }

        return responseString;
    }

}
