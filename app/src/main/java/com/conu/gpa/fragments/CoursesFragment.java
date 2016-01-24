package com.conu.gpa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.conu.gpa.Globals;
import com.conu.gpa.R;
import com.conu.gpa.adapters.CourseAdapter;
import com.conu.gpa.classes.Course;
import com.conu.gpa.networking.GPAAPI;

import java.util.LinkedList;

public class CoursesFragment extends Fragment {

    public View root;
    public CourseAdapter listAdapter;
    public ListView lv;
    public LinkedList<Course> courses = new LinkedList<>();
    public SwipeRefreshLayout srl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_courses, container, false);

        Globals.user.courses = new LinkedList<>();
        srl = (SwipeRefreshLayout) root.findViewById(R.id.activity_main_swipe_refresh_layout);
        srl.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        srl.setRefreshing(true);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });

        lv = (ListView) root.findViewById(android.R.id.list);
        GPAAPI.GetAllCourses(getContext(), this);
        GPAAPI.GetUserCourses(getContext(), this);

        return root;
    }

    public void reload(){
        srl.setEnabled(false);
        courses.clear();
        Globals.user.courses = new LinkedList<>();
        listAdapter = new CourseAdapter(getContext(), courses, this);
        lv.setAdapter(listAdapter);
        GPAAPI.GetAllCourses(getContext(), this);
        GPAAPI.GetUserCourses(getContext(), this);
    }

    public void populateList(){
        srl.setEnabled(true);
        srl.setRefreshing(false);
        listAdapter = new CourseAdapter(getContext(), courses, this);
        lv.setAdapter(listAdapter);
        markCourses();
    }

    public void markCourses(){
        if(listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

}
