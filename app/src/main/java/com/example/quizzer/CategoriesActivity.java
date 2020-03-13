package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    private RecyclerView categoriesListRv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Toolbar toolbar = (Toolbar) findViewById(R.id.categoriesToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categoriesListRv = (RecyclerView) findViewById(R.id.categoriesRV);

        LinearLayoutManager layoutManager = new LinearLayoutManager(CategoriesActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        categoriesListRv.setLayoutManager(layoutManager);

        List<CategoryModel> list = new ArrayList<>();
        list.add(new CategoryModel("", "Category 1"));
        list.add(new CategoryModel("", "Category 2"));
        list.add(new CategoryModel("", "Category 3"));
        list.add(new CategoryModel("", "Category 4"));
        list.add(new CategoryModel("", "Category 5"));
        list.add(new CategoryModel("", "Category 6"));
        list.add(new CategoryModel("", "Category 7"));

        CategoryAdapter adapter = new CategoryAdapter(list);
        categoriesListRv.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
