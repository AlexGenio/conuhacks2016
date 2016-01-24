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
import com.conu.gpa.adapters.PeopleAdapter;
import com.conu.gpa.classes.Course;
import com.conu.gpa.classes.Student;
import com.conu.gpa.networking.GPAAPI;

import java.util.LinkedList;

public class PeopleFragment extends Fragment {

    public View root;
    public PeopleAdapter listAdapter;
    public ListView lv;
    public LinkedList<Student> people = new LinkedList<>();
    public SwipeRefreshLayout srl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_people, container, false);

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
        GPAAPI.GetPeople(getContext(), this);

        return root;
    }

    public void reload(){
        srl.setEnabled(false);
        people.clear();
        Globals.user.courses = new LinkedList<>();
        listAdapter = new PeopleAdapter(getContext(), people, this);
        lv.setAdapter(listAdapter);
        GPAAPI.GetPeople(getContext(), this);
    }

    public void populateList(){
        srl.setEnabled(true);
        srl.setRefreshing(false);
        listAdapter = new PeopleAdapter(getContext(), people, this);
        lv.setAdapter(listAdapter);
        markCourses();
    }

    public void markCourses(){
        if(listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

}
