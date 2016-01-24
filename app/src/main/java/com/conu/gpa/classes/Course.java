package com.conu.gpa.classes;

public class Course {
    public String name;
    public Integer id;
    public String schoolName;

    public Course (){}
    public Course (String name, String school, Integer id){
        this.name = name;
        this.schoolName = school;
        this.id = id;
    }

}
