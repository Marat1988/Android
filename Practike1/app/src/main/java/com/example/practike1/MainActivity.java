package com.example.practike1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
    private final int MIN_POINTS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewSalaryText = findViewById(R.id.textViewSalaryText);
        seekBarSalary = findViewById(R.id.seekBarSalary);
        textViewSalaryText.setText(String.format("Уровень ЗП: %s", seekBarSalary.getProgress()));
        constraintLayout = findViewById(R.id.constraintLayout);
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
                    Toast.makeText(getApplicationContext(), "Да", Toast.LENGTH_SHORT).show();
                }
            }
        });
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