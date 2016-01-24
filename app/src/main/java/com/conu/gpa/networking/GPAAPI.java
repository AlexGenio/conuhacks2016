package com.conu.gpa.networking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.conu.gpa.Globals;
import com.conu.gpa.LoginActivity;
import com.conu.gpa.classes.Student;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GPAAPI {

    public static void Login(final Context c, final String username,
                             final String password, final LoginActivity a){

        RequestQueue request = Volley.newRequestQueue(c);

        StringRequest postRequest = new StringRequest(Request.Method.POST, Globals.BASE_URL + "login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            if(!root.has("error") && root.has("token")){
                                Globals.user = new Student();
                                Globals.user.username = root.getString("username");
                                Globals.user.pictureLink = root.getString("picture");
                                Globals.user.name = root.getString("name");
                                Globals.user.schoolName = root.getString("school");
                                Globals.user.description = root.getString("description");
                                Globals.saveUser(c, a);
                                Globals.setToken(c, a, root.getString("token"));
                                a.afterSuccess();
                            }else{
                                a.afterFailure();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        request.add(postRequest);
    }

    public static void Logout(final Context c, final String token, final Activity a) {
        RequestQueue request = Volley.newRequestQueue(c);

        StringRequest postRequest = new StringRequest(Request.Method.POST, Globals.BASE_URL + "logout.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(a, LoginActivity.class);
                        a.startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("token", token);
                return params;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        request.add(postRequest);
    }

}
