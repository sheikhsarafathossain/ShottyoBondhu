package com.team_c.shottyobondhu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    private RadioGroup rgQ1, rgQ2, rgQ3;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        rgQ1 = findViewById(R.id.rg_q1);
        rgQ2 = findViewById(R.id.rg_q2);
        rgQ3 = findViewById(R.id.rg_q3);
        btnSubmit = findViewById(R.id.btn_submit_quiz);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateScore();
            }
        });
    }

    private void calculateScore() {
        int score = 0;
        int total = 3;

        // Question 1: Reverse Image Search (rb_q1_a)
        if (rgQ1.getCheckedRadioButtonId() == R.id.rb_q1_a) {
            score++;
        }

        // Question 2: True (rb_q2_a)
        if (rgQ2.getCheckedRadioButtonId() == R.id.rb_q2_a) {
            score++;
        }

        // Question 3: Sensational (rb_q3_b)
        if (rgQ3.getCheckedRadioButtonId() == R.id.rb_q3_b) {
            score++;
        }

        // Check if all answered (optional, but good UX)
        if (rgQ1.getCheckedRadioButtonId() == -1 ||
                rgQ2.getCheckedRadioButtonId() == -1 ||
                rgQ3.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please answer all questions!", Toast.LENGTH_SHORT).show();
            return;
        }

        showResultDialog(score, total);
    }

    private void showResultDialog(int score, int total) {
        String title = "Quiz Completed!";
        String message = "You scored " + score + " out of " + total + ".";

        if (score == total) {
            message += "\n\nAwesome work! You rely on facts!";
        } else if (score >= 1) {
            message += "\n\nGood effort! Keep checking your sources.";
        } else {
            message += "\n\nKeep learning about fake news to improve.";
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    finish(); // Close quiz logic
                })
                .setCancelable(false)
                .show();
    }
}
