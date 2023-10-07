package com.example.practike2;

import android.annotation.SuppressLint;
import android.widget.EditText;
import android.widget.TextView;

public class CurrencyConvertor {
    private EditText editTextValue;
    private TextView textViewCourse;
    private double valueCourse;

    @SuppressLint("SetTextI18n")
    public CurrencyConvertor(EditText editTextValue, TextView textViewCourse, double valueCourse) {
        this.editTextValue = editTextValue;
        this.textViewCourse = textViewCourse;
        this.valueCourse = valueCourse;
        textViewCourse.setText(Double.toString(valueCourse));
    }


    @SuppressLint("SetTextI18n")
    public void setValueCourseEditText(double value) {
        double currencyValue = valueCourse * value;
        double scale = Math.pow(10, 2);
        double result = Math.ceil(currencyValue * scale) / scale;
        editTextValue.setText(Double.toString(result));
    }
}
