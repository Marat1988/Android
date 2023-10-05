package com.example.practike1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBarSalary;
    private TextView textViewSalaryText;
    private ConstraintLayout constraintLayout;
    private EditText editTextFullName;
    private EditText editTextAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewSalaryText = findViewById(R.id.textViewSalaryText);
        seekBarSalary = findViewById(R.id.seekBarSalary);
        textViewSalaryText.setText(String.format("Уровень ЗП: %s", seekBarSalary.getProgress()));
        constraintLayout = findViewById(R.id.constraintLayout);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextAge = findViewById(R.id.editTextAge);
        seekBarSalary.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textViewSalaryText.setText(String.format("Уровень ЗП: %s $", i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void onClickSendAnswer(View view) {
        if (editTextFullName.getText().toString().equals("") && editTextAge.getText().toString().equals("")) {
            Toast.makeText(this, "Введите ФИО и возраст", Toast.LENGTH_SHORT).show();
        } else {
            Applicant applicant = new Applicant(
                    ((EditText) findViewById(R.id.editTextFullName)).getText().toString(),
                    Integer.parseInt(((EditText) findViewById(R.id.editTextAge)).getText().toString()),
                    seekBarSalary.getProgress(),
                    ((CheckBox) findViewById(R.id.checkWorkExperience)).isChecked(),
                    ((CheckBox) findViewById(R.id.checkWorkTeamDevelopmentSkills)).isChecked(),
                    ((CheckBox) findViewById(R.id.checkWillingnessTraver)).isChecked()
            );
            List<RadioGroup> radioGroups = getAllRadioGroups(constraintLayout);
            radioGroups.forEach(radioGroup -> {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(radioButtonId);
                if (radioButton.getTag() != null) {
                    if (radioButton.getTag().toString().equals("rightAnswer")) {
                        applicant.setCountPoint(applicant.getCountPoint() + 2);
                    }
                }
            });
            Toast.makeText(getApplicationContext(), String.format("Количество баллов: %s", applicant.getCountPoint()), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ActivityAnswer.class);
            intent.putExtra("fullname", applicant.getFullName());
            intent.putExtra("age", applicant.getAge());
            intent.putExtra("points", applicant.getCountPoint());
            startActivity(intent);
        }
    }

    public List<RadioGroup> getAllRadioGroups(ViewGroup layout) {
        List<RadioGroup> grp = new ArrayList<>();
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v instanceof RadioGroup) {
                grp.add((RadioGroup) v);
            }
        }
        return grp;
    }
}