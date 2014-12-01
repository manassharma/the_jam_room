package com.example.lenovo.thejamroom.async;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.lenovo.thejamroom.Constants;
import com.example.lenovo.thejamroom.activity.HomeActivity;
import com.example.lenovo.thejamroom.util.UserSession;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by avaneeshdesai on 11/27/14.
 */
public class AuthenticateUser extends AsyncTask<String, Void, String> {
    Context activity;
   public AuthenticateUser(Context context){
        activity = context;
    }
    @Override
    protected String doInBackground(String... strings) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Constants.SERVER_URL+Constants.AUTH_URL);
        String responseString = null;
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("name", UserSession.getName()));
            nameValuePairs.add(new BasicNameValuePair("email", UserSession.getUsername()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
                Log.d("Response Text", responseString);
                return responseString;
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        activity.startActivity(new Intent(activity, HomeActivity.class));
    }
}
