package com.example.quizzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class QuestionsActivity extends AppCompatActivity {

    private TextView txtQuestion, txtScore;
    private FloatingActionButton fltBookMark;
    private LinearLayout optionContainer;
    private Button btnShare, btnNext;
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.questionToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        txtScore = (TextView) findViewById(R.id.txtScore);
        fltBookMark = (FloatingActionButton) findViewById(R.id.fltBookmark);
        optionContainer = (LinearLayout) findViewById(R.id.optionsLayout);
        btnShare = (Button) findViewById(R.id.btnShare);
        btnNext = (Button) findViewById(R.id.btnNext);
    }

    private void playAnim(final View view, final int value){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (value == 0 && count < 4){
                    playAnim(optionContainer.getChildAt(count), 0);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // Data Change
                if (value == 0){
                    playAnim(view, 1);
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
}
