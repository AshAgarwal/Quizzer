package com.example.quizzer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridSetAdapter extends BaseAdapter {

    private int sets = 0;
    private String category;

    public GridSetAdapter(int sets, String title)
    {
        this.sets = sets;
        this.category = title;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view;

        if (convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_list_item, parent, false);
        } else {
            view = convertView;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent questionIntent = new Intent(parent.getContext(), QuestionsActivity.class);
                questionIntent.putExtra("category", category);
                questionIntent.putExtra("setNo", position + 1);
                parent.getContext().startActivity(questionIntent);
            }
        });

        ((TextView) view.findViewById(R.id.textSetNum)).setText(String.valueOf(position + 1));

        return view;
    }
}
