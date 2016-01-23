package com.conu.gpa.classes;

import android.graphics.Bitmap;

import java.util.LinkedList;

public class Student {
    public String username;
    public String name;
    public Bitmap picture;
    public String schoolName;

    public LinkedList<Course> courses;

    public Student(){
        username = "";
        name = "";
        picture = null;
        schoolName = "";
        courses = new LinkedList<>();
    }
}
