package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

    private static final String FILE_NAME = "QUIZZER";
    private static final String KEY_NAME = "QUESTIONS";

    private TextView txtQuestion, txtScore;
    private FloatingActionButton fltBookMark;
    private LinearLayout optionContainer;
    private Button btnShare, btnNext;
    private int count = 0;
    private int position = 0;
    private List<QuestionModelClass> list;
    private int score = 0;

    private String category;
    private int setsNo;

    private Dialog loadingDialog;

    private DatabaseReference databaseReference;

    List<QuestionModelClass> bookMarkList;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    private int matchedQuestionPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.questionToolbar);
        setSupportActionBar(toolbar);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialogue);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        txtScore = (TextView) findViewById(R.id.txtScore);
        fltBookMark = (FloatingActionButton) findViewById(R.id.fltBookmark);
        optionContainer = (LinearLayout) findViewById(R.id.optionsLayout);
        btnShare = (Button) findViewById(R.id.btnShare);
        btnNext = (Button) findViewById(R.id.btnNext);

        preferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();

        getBookmarks();

        fltBookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modelMatch()){
                    bookMarkList.remove(matchedQuestionPosition);
                    fltBookMark.setImageDrawable(getDrawable(R.drawable.bookmark_icon));
                } else {
                    bookMarkList.add(list.get(position));
                    fltBookMark.setImageDrawable(getDrawable(R.drawable.bookmarked));
                }
            }
        });

        category = getIntent().getStringExtra("category");
        setsNo = getIntent().getIntExtra("setNo", 1);

        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        loadingDialog.show();
        databaseReference.child("Sets").child(category).child("Questions")
                .orderByChild("setNo").equalTo(setsNo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            list.add(snapshot.getValue(QuestionModelClass.class));
                        }

                        if (list.size() > 0){
                            for (int i = 0; i < 4; i++){
                                optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        checkAnswer((Button)v);
                                    }
                                });
                            }

                            playAnim(txtQuestion, 0, list.get(position).getQuestion());

                            btnNext.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    btnNext.setEnabled(false);
                                    btnNext.setAlpha(0.7f);
                                    enableOption(true);
                                    position++;
                                    if (position == list.size()){
                                        // scoreActivity
                                        Intent scoreIntent = new Intent(QuestionsActivity.this, ScoreActivity.class);
                                        scoreIntent.putExtra("Score", score);
                                        scoreIntent.putExtra("total", list.size());
                                        startActivity(scoreIntent);
                                        finish();
                                        return;
                                    }
                                    count = 0;
                                    playAnim(txtQuestion, 0, list.get(position).getQuestion());
                                }
                            });

                        } else {
                            finish();
                            Toast.makeText(QuestionsActivity.this, "No Question Available", Toast.LENGTH_SHORT).show();
                        }

                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("QUESTION ACT CANCELLED:", databaseError.getMessage() );
                        loadingDialog.dismiss();
                        finish();
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
    }

    private void playAnim(final View view, final int value, final String data){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (value == 0 && count < 4){
                    String option = "";
                    if (count == 0){
                        option = list.get(position).getOptionA();
                    } else if (count == 1){
                        option = list.get(position).getOptionB();
                    } else if (count == 2){
                        option = list.get(position).getOptionC();
                    } else if (count == 3){
                        option = list.get(position).getOptionD();
                    }
                    playAnim(optionContainer.getChildAt(count), 0, option);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (value == 0){

                    try{
                        ((TextView)view).setText(data);
                        txtScore.setText(position + 1 + "/" + list.size());

                        if (modelMatch()){
                            fltBookMark.setImageDrawable(getDrawable(R.drawable.bookmarked));
                        } else {
                            fltBookMark.setImageDrawable(getDrawable(R.drawable.bookmark_icon));
                        }

                    } catch (ClassCastException ex){
                        ((Button)view).setText(data);
                    }

                    view.setTag(data);
                    playAnim(view, 1, data);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void checkAnswer(Button selectedOption){
        enableOption(false);
        btnNext.setEnabled(true);
        btnNext.setAlpha(1);
        if (selectedOption.getText().toString().equals(list.get(position).getCorrentAns())){
            // correct
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            score++;
        } else {
            //incorrent
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));

            Button correctOpt = (Button) optionContainer.findViewWithTag(list.get(position).getCorrentAns());
            correctOpt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }


    }

    private void enableOption(boolean enable){
        for (int i = 0; i < 4; i++){
            optionContainer.getChildAt(i).setEnabled(enable);

            if (enable){
                optionContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
            }
        }
    }

    private void getBookmarks(){
        String json = preferences.getString(KEY_NAME, "");
        Type type = new TypeToken<List<QuestionModelClass>>(){}.getType();

        bookMarkList = gson.fromJson(json, type);

        if (bookMarkList == null){
            bookMarkList = new ArrayList<>();
        }
    }

    private boolean modelMatch(){
        boolean matched = false;
        int i = 0;
        for (QuestionModelClass modelClass : bookMarkList){
            if (modelClass.getQuestion().equals(list.get(position).getQuestion()) &&
                modelClass.getCorrentAns().equals(list.get(position).getCorrentAns()) &&
                modelClass.getSetNo() == list.get(position).getSetNo()){
                matched = true;
                matchedQuestionPosition = i;
            }
            i++;
        }

        return matched;
    }

    private void storeBookmarks(){
        String json = gson.toJson(bookMarkList);
        editor.putString(KEY_NAME, json);
        editor.commit();
    }
}
