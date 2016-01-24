package com.conu.gpa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.conu.gpa.R;
import com.conu.gpa.classes.Student;
import com.conu.gpa.fragments.PeopleFragment;

import java.util.LinkedList;

public class PeopleAdapter extends ArrayAdapter<Student> {

    private final static int REGULAR_ROW_LAYOUT = R.layout.row_people;

    LinkedList<Student> students;
    PeopleFragment parent;

    public PeopleAdapter (Context activity, LinkedList<Student> list, PeopleFragment parent){
        super(activity, REGULAR_ROW_LAYOUT, list);
        students = list;
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

        final Student curr = students.get(position);

        ((TextView) rowView.findViewById(R.id.username)).setText(curr.username);
        ((TextView) rowView.findViewById(R.id.name)).setText(curr.name);
        ((TextView) rowView.findViewById(R.id.deets)).setText("Bio: " + curr.description);

        if(curr.picture != null){
            ((ImageView) rowView.findViewById(R.id.pic)).setImageBitmap(curr.picture);
        }

        return rowView;
    }

}
