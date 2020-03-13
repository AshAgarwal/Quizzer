package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

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

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.questionToolbar);
        setSupportActionBar(toolbar);

        txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        txtScore = (TextView) findViewById(R.id.txtScore);
        fltBookMark = (FloatingActionButton) findViewById(R.id.fltBookmark);
        optionContainer = (LinearLayout) findViewById(R.id.optionsLayout);
        btnShare = (Button) findViewById(R.id.btnShare);
        btnNext = (Button) findViewById(R.id.btnNext);

        category = getIntent().getStringExtra("category");
        setsNo = getIntent().getIntExtra("setNo", 1);

        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();

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
                                        // startActivity
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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("QUESTION ACT CANCELLED:", databaseError.getMessage() );
                    }
                });
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
}
