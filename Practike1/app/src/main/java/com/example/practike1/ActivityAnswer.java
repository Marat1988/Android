package com.example.practike1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityAnswer extends AppCompatActivity {

    private final int MIN_POINTS = 10;
    private TextView textViewAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        textViewAnswer = findViewById(R.id.textViewAnswer);
        Intent intent = getIntent();
        if (intent.hasExtra("fullname") && intent.hasExtra("age") && intent.hasExtra("points")) {
            String fullName = intent.getStringExtra("fullname");
            int age = intent.getIntExtra("age", 18);
            int points = intent.getIntExtra("points", 0);
            if (age < 18 || age > 40) {
                textViewAnswer.setText(String.format("Здравствуйте %s. Вы нам не подходите, т.к. мы ищем сотрудников от 18 до 40 лет", fullName));
            } else if (points < MIN_POINTS) {
                textViewAnswer.setText(String.format("Здравствуйте %s. Вы нам не подходите, так как вы набрали %s баллов. Для прохождения нужно набрать %s баллов", fullName, points, MIN_POINTS));
            } else {
                textViewAnswer.setText(String.format("Здравствуйте %s. Вы набрали %s баллов. Добро пожаловать в нашу корпорацию \"Панама\"", fullName, points));
            }
        }
    }
}