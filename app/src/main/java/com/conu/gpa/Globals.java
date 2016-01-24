package com.conu.gpa;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.conu.gpa.classes.Course;
import com.conu.gpa.classes.Student;

import java.util.LinkedList;

public class Globals {

    public static Student user;
    public static String BASE_URL = "http://ec2-54-174-156-181.compute-1.amazonaws.com/prod/";
    public static String MEDIA_URL = "http://ec2-54-174-156-181.compute-1.amazonaws.com/";

    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        /*canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);*/
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public static String getToken(Context c, Activity a){
        SharedPreferences prefs = a.getSharedPreferences(
                c.getPackageName(), Context.MODE_PRIVATE);
        return prefs.getString("token", "");
    }

    public static void setToken(Context c, Activity a, String token){
        SharedPreferences prefs = a.getSharedPreferences(
                c.getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putString("token", token).apply();
    }

    public static void saveUser(Context c, Activity a) throws NullPointerException{
        SharedPreferences prefs = a.getSharedPreferences(
                c.getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putString("username", Globals.user.username)
                .putString("name", Globals.user.name)
                .putString("pictureLink", Globals.user.pictureLink)
                .putString("description", Globals.user.description)
                .putString("school", Globals.user.schoolName).apply();
    }

    public static void loadUser(Context c, Activity a){
        SharedPreferences prefs = a.getSharedPreferences(
                c.getPackageName(), Context.MODE_PRIVATE);
        Globals.user = new Student();
        Globals.user.username = prefs.getString("username", "");
        Globals.user.name = prefs.getString("name", "");
        Globals.user.description = prefs.getString("description", "");
        Globals.user.pictureLink = prefs.getString("pictureLink", "");
        Globals.user.schoolName = prefs.getString("school", "");
    }

    public static LinkedList<Course> removeClass(LinkedList<Course> courses, int id){
        LinkedList<Course> n_courses = new LinkedList<>();
        for(Course c : courses){
            if(c.id != id){
                n_courses.add(c);
            }
        }
        return n_courses;
    }

    public static boolean in(LinkedList<Course> courses, int id){
        for(Course c : courses){
            if(c.id == id){
                return true;
            }
        }
        return false;
    }

    public static boolean in(LinkedList<Course> courses, LinkedList<Course> courses_2){
        for(Course c : courses){
            for(Course c2 : courses_2){
                if(c.id.equals(c2.id)){
                    return true;
                }
            }
        }
        return false;
    }

}
