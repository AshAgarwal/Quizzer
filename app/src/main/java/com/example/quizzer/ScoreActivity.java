package com.example.quizzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    private Toolbar scoreToolbar;
    private TextView txtCurrScore;
    private TextView txtTotal;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        scoreToolbar = (Toolbar) findViewById(R.id.scoreToolbar);
        setSupportActionBar(scoreToolbar);

        txtCurrScore = (TextView) findViewById(R.id.txtCurrScore);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        btnDone = (Button) findViewById(R.id.btnDone);

        txtCurrScore.setText(String.valueOf(getIntent().getIntExtra("Score", 0)));
        txtTotal.setText("Out of " + String.valueOf(getIntent().getIntExtra("total", 0)));
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
