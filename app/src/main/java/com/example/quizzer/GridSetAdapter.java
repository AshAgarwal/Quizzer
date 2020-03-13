package com.example.quizzer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridSetAdapter extends BaseAdapter {

    private int sets = 0;

    public GridSetAdapter(int sets) {
        this.sets = sets;
    }

    @Override
    public int getCount() {
        return sets;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_list_item, parent, false);
        } else {
            view = convertView;
        }

        ((TextView) view.findViewById(R.id.textSetNum)).setText(String.valueOf(position + 1));

        return view;
    }
}
