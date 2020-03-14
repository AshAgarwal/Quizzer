package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookMarkActivity extends AppCompatActivity {

    private static final String FILE_NAME = "QUIZZER";
    private static final String KEY_NAME = "QUESTIONS";

    private Toolbar toolbar;
    private RecyclerView mBookmarkRV;

    List<QuestionModelClass> bookMarkList;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);
        toolbar = (Toolbar) findViewById(R.id.bookMarkToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bookmarks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBookmarkRV = (RecyclerView) findViewById(R.id.bookMarkRV);

        preferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();

        getBookmarks();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mBookmarkRV.setLayoutManager(manager);

        BookmarkAdapter adapter = new BookmarkAdapter(bookMarkList);
        mBookmarkRV.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getBookmarks(){
        String json = preferences.getString(KEY_NAME, "");
        Type type = new TypeToken<List<QuestionModelClass>>(){}.getType();

        bookMarkList = gson.fromJson(json, type);

        if (bookMarkList == null){
            bookMarkList = new ArrayList<>();
        }
    }

    private void storeBookmarks(){
        String json = gson.toJson(bookMarkList);
        editor.putString(KEY_NAME, json);
        editor.commit();
    }
}
