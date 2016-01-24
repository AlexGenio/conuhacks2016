package com.conu.gpa.networking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.util.Base64;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.conu.gpa.Globals;
import com.conu.gpa.LoginActivity;
import com.conu.gpa.adapters.PeopleAdapter;
import com.conu.gpa.classes.Course;
import com.conu.gpa.RegisterActivity;
import com.conu.gpa.classes.Student;
import com.conu.gpa.fragments.CoursesFragment;
import com.conu.gpa.fragments.PeopleFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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
    public static void createAccount(final Context c, final String username, final Bitmap picture,
                                     final String password, final String school, final String name, final String description, final RegisterActivity a)
    {
        RequestQueue request = Volley.newRequestQueue(c);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Globals.BASE_URL + "register.php",new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    if(!root.has("error") && root.has("token")){
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
                String ba1;
                Map<String, String>  params = new HashMap<>();
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                picture.compress(Bitmap.CompressFormat.JPEG, 50, bao);
                byte[] ba = bao.toByteArray();
                ba1 = Base64.encodeToString(ba,Base64.DEFAULT);
                // the POST parameters:
                params.put("username", username);
                params.put("password", password);
                params.put("name",name);
                params.put("school",school);
                params.put("picture",ba1);
                params.put("description",description);
                return params;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        request.add(postRequest);
    }

    public static void GetImage(Context c, final ImageView img, String url){
        RequestQueue queue = Volley.newRequestQueue(c);
        ImageLoader imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }
        });
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap b = response.getBitmap();
                if (b != null) {
                    img.setImageBitmap(Globals.getCircleBitmap(b));
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
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

    public static void GetUserCourses(final Context c, final CoursesFragment frag){
        RequestQueue request = Volley.newRequestQueue(c);

        StringRequest postRequest = new StringRequest(Request.Method.POST, Globals.BASE_URL + "getUserCourses.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject root = new JSONObject(response);
                            JSONArray courses = root.getJSONArray("courses");

                            for(int i = 0; i < courses.length(); i++){
                                JSONObject curr = courses.getJSONObject(i);
                                Globals.user.courses.add(new Course(
                                        curr.getString("name"),
                                        Globals.user.schoolName,
                                        curr.getInt("id")
                                ));
                            }

                            frag.markCourses();

                        } catch (JSONException e){
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
                params.put("token", Globals.getToken(c, frag.getActivity()));
                return params;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        request.add(postRequest);
    }

    public static void GetAllCourses(final Context c, final CoursesFragment frag){
        RequestQueue request = Volley.newRequestQueue(c);

        StringRequest postRequest = new StringRequest(Request.Method.POST, Globals.BASE_URL + "getSchoolCourses.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject root = new JSONObject(response);
                            JSONArray courses = root.getJSONArray("courses");

                            for(int i = 0; i < courses.length(); i++){
                                JSONObject curr = courses.getJSONObject(i);
                                frag.courses.add(new Course(
                                        curr.getString("name"),
                                        Globals.user.schoolName,
                                        curr.getInt("id")
                                ));
                            }

                            frag.populateList();

                        } catch (JSONException e){
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
                params.put("token", Globals.getToken(c, frag.getActivity()));
                return params;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        request.add(postRequest);
    }

    public static void RemoveCourse(final Context c, final CoursesFragment frag, final Integer id){
        RequestQueue request = Volley.newRequestQueue(c);

        StringRequest postRequest = new StringRequest(Request.Method.POST, Globals.BASE_URL + "removeCourses.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Globals.user.courses = Globals.removeClass(Globals.user.courses, id);
                        frag.markCourses();

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
                params.put("token", Globals.getToken(c, frag.getActivity()));
                params.put("CID", id.toString());
                return params;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        request.add(postRequest);
    }

    public static void AddCourse(final Context c, final CoursesFragment frag, final String name){
        RequestQueue request = Volley.newRequestQueue(c);

        StringRequest postRequest = new StringRequest(Request.Method.POST, Globals.BASE_URL + "createClass.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject curr = new JSONObject(response);
                            Globals.user.courses.add(new Course(
                                    curr.getString("class"),
                                    Globals.user.schoolName,
                                    curr.getInt("id")
                            ));

                            frag.markCourses();

                        } catch (JSONException e){
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
                params.put("token", Globals.getToken(c, frag.getActivity()));
                params.put("class", name);
                return params;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        request.add(postRequest);
    }

    public static void GetPeople(final Context c, final PeopleFragment frag){
        RequestQueue request = Volley.newRequestQueue(c);

        StringRequest postRequest = new StringRequest(Request.Method.POST, Globals.BASE_URL + "findStudentsByCourse.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject root = new JSONObject(response);
                            JSONArray courses = root.getJSONArray("students");

                            for(int i = 0; i < courses.length(); i++){
                                JSONObject curr = courses.getJSONObject(i);
                                frag.people.add(new Student(
                                        curr.getString("username"),
                                        curr.getString("name"),
                                        curr.getString("picture"),
                                        Globals.user.schoolName,
                                        curr.getString("description")
                                ));
                                GPAAPI.GetImage(c, frag.people.getLast(), frag);
                            }

                            frag.populateList();

                        } catch (JSONException e){
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
                params.put("token", Globals.getToken(c, frag.getActivity()));
                return params;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        request.add(postRequest);
    }

    public static void GetImage(Context c, final Student u, final PeopleFragment parent){
        RequestQueue queue = Volley.newRequestQueue(c);
        ImageLoader imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }
        });
        imageLoader.get(Globals.MEDIA_URL + u.pictureLink, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap b = response.getBitmap();
                if (b != null) {
                    u.picture = Globals.getCircleBitmap(b);
                }
                parent.markCourses();
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

}
