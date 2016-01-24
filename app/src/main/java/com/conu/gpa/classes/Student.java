package com.conu.gpa.classes;

import android.graphics.Bitmap;

import java.util.LinkedList;

public class Student {
    public String username;
    public String name;
    public Bitmap picture;
    public String pictureLink;
    public String schoolName;
    public String description;

    public LinkedList<Course> courses;

    public Student(){
        username = "";
        name = "";
        picture = null;
        pictureLink = "";
        schoolName = "";
        description = "";
        courses = new LinkedList<>();
    }
}
