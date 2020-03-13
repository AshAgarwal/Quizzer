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

public class BookMarkActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView mBookmarkRV;

    private List<QuestionModelClass> listt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);
        toolbar = (Toolbar) findViewById(R.id.bookMarkToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bookmarks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBookmarkRV = (RecyclerView) findViewById(R.id.bookMarkRV);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mBookmarkRV.setLayoutManager(manager);

        listt = new ArrayList<>();
        listt.add(new QuestionModelClass("What is Your Name?", "", "", "", "", "Ashutosh Agarwal", 0));
        listt.add(new QuestionModelClass("What is Your Name?", "", "", "", "", "Ashutosh Agarwal", 0));
        listt.add(new QuestionModelClass("What is Your Name?", "", "", "", "", "Ashutosh Agarwal", 0));
        listt.add(new QuestionModelClass("What is Your Name?", "", "", "", "", "Ashutosh Agarwal", 0));
        listt.add(new QuestionModelClass("What is Your Name?", "", "", "", "", "Ashutosh Agarwal", 0));
        listt.add(new QuestionModelClass("What is Your Name?", "", "", "", "", "Ashutosh Agarwal", 0));

        BookmarkAdapter adapter = new BookmarkAdapter(listt);
        mBookmarkRV.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
