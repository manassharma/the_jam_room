package com.example.lenovo.thejamroom.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.lenovo.thejamroom.async.AuthenticateUser;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

/**
 * Created by avaneeshdesai on 11/27/14.
 */
public class UserSession {
    static Session session;
    static String name;
    static String username;
    static String accessToken;
    static String userId;
    static Context context;

    public static void setSession(Session s, Context ctx){
        context = ctx;
        if(session == null) {
            session = s;

            makeMeRequest();
        }
    }

   public static Session getSession(){
       if(session!=null)
             return session;

       Log.e("USERSESSION", "Session is null");
       return null;
    }

    public static void setName(String n){
        name = n;
    }

    public static String getName(){
        return name;
    }

    public static void setUsername(String uname){
        username = uname;
    }

    public static String getUsername(){
        return username;
    }

    public static void setAccessToken(String accessToken1){
        SharedPreferences.Editor editor = context.getSharedPreferences("jamroom",context.MODE_PRIVATE).edit();
        editor.putString("accessToken",accessToken1);
        editor.commit();
        accessToken = accessToken1;
    }

    public static String getAccessToken(){
        return accessToken;
    }


    public static void setUserId(String id){
        userId = id;
    }

    public static String getUserId(){
        return userId;
    }

    private static void makeMeRequest() {
        final Session session = UserSession.getSession();
        // Make an API call to get user data and define a
        // new callback to handle the response.
        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        // If the response is successful
                            if (user != null) {
                                UserSession.name = user.getName();
                                UserSession.userId = user.getId();
                                UserSession.username = user.asMap().get("email").toString();
                                System.out.println(name+ "  "+ username+ "  "+userId);

                                if(context.getSharedPreferences("jamroom", context.MODE_PRIVATE).getString("accessToken",null) == null){
                                    try {
                                        AuthenticateUser authenticateUser = new AuthenticateUser(context);
                                        authenticateUser.execute();
                                        String res = authenticateUser.get();
                                        UserSession.setAccessToken(res);
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                            }
                        if (response.getError() != null) {
                            // Handle errors, will do so later.
                        }
                    }
                });
        try {
            request.executeAsync().get();
        }
        catch (Exception e){ e.printStackTrace();}
    }
}
