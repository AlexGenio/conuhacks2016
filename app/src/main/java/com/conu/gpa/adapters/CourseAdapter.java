package com.conu.gpa.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.conu.gpa.Globals;
import com.conu.gpa.R;
import com.conu.gpa.classes.Course;
import com.conu.gpa.fragments.CoursesFragment;
import com.conu.gpa.networking.GPAAPI;

import java.util.LinkedList;

public class CourseAdapter extends ArrayAdapter<Course> {

    private final static int REGULAR_ROW_LAYOUT = R.layout.row_course;

    LinkedList<Course> courses;
    CoursesFragment parent;

    public CourseAdapter (Context activity, LinkedList<Course> list, CoursesFragment parent){
        super(activity, REGULAR_ROW_LAYOUT, list);
        courses = list;
        this.parent = parent;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup par) {
        View rowView = convertView;
        if (rowView == null) { // first time: inflate a new View
            Context context = getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
            );
            rowView = inflater.inflate(REGULAR_ROW_LAYOUT, null);
        }

        final Course curr = courses.get(position);
        if(Globals.in(Globals.user.courses, curr.id)){
            rowView.findViewById(R.id.cont).setBackgroundColor(
                    ContextCompat.getColor(parent.getContext(), R.color.colorAccent));
            ((TextView) rowView.findViewById(R.id.name)).setTextColor(
                    ContextCompat.getColor(parent.getContext(), R.color.white));
            ((TextView) rowView.findViewById(R.id.school)).setTextColor(
                    ContextCompat.getColor(parent.getContext(), R.color.white));
        }else{
            rowView.findViewById(R.id.cont).setBackgroundColor(
                    ContextCompat.getColor(parent.getContext(), R.color.white));
            ((TextView) rowView.findViewById(R.id.name)).setTextColor(
                    ContextCompat.getColor(parent.getContext(), R.color.grey));
            ((TextView) rowView.findViewById(R.id.school)).setTextColor(
                    ContextCompat.getColor(parent.getContext(), R.color.lighter_grey));
        }

        rowView.findViewById(R.id.cont).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Globals.in(Globals.user.courses, curr.id)) {
                    GPAAPI.RemoveCourse(parent.getContext(), parent, curr.id);
                }else{
                    GPAAPI.AddCourse(parent.getContext(), parent, curr.name);
                }
            }
        });

        ((TextView) rowView.findViewById(R.id.name)).setText(courses.get(position).name);
        ((TextView) rowView.findViewById(R.id.school)).setText(courses.get(position).schoolName);

        return rowView;
    }

}
