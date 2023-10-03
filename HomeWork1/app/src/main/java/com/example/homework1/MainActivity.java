package com.example.homework1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private long time = 0;
    private Button buttonClick;
    private final String INFO = "LiveCycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonClick = findViewById(R.id.buttonStart);
        time = System.currentTimeMillis();
        Log.i(INFO, String.format("OnCreate. Activity создано. Текущее время (в м.с.) %s", time));
        buttonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("LiveCycle", "Ура! У меня нажата кнопка");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        showMessage("onStart. Activity становиться видимым. Время запуска %s. Разница во времени между запусками активностей равно %s ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showMessage("onResume. Activity получает фокус. Время запуска %s. Разница во времени между запусками активностей равно %s ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        showMessage("onPause. Activity приостановлено. Время запуска %s. Разница во времени между запусками активностей равно %s ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        showMessage("onStop. Activity остановлено. Время запуска %s. Разница во времени между запусками активностей равно %s ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showMessage("onDestroy. Activity уничтожено. Время запуска %s. Разница во времени между запусками активностей равно %s ");
    }

    private void showMessage(String message) {
        long time = System.currentTimeMillis();
        Log.i(INFO, String.format(message, time, (time - this.time)));
        this.time = time;
    }
}