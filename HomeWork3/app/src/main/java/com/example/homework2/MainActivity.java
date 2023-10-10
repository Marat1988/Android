package com.example.homework2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private EditText editTextInputResult;
    private Calculator calculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextInputResult = findViewById(R.id.editTextInputResult);
        calculator = new Calculator();
    }

    public void onClickClear(View view) { //Очистка текстового поля
        editTextInputResult.setText("");
        calculator.defaultValue();
    }

    @SuppressLint("SetTextI18n")
    public void onClickArithmeticOperation(View view) { //+ - * /
        Button button = (Button) view;
        if (calculator.number1 != null && calculator.number2 == null) {
            calculator.operation = button.getText().toString();
            editTextInputResult.setText(calculator.number1 + " " + calculator.operation);
        }
        if (calculator.number1 != null && calculator.number2 != null && calculator.operation != null) {
            onClickCalcResult(view);
        }
    }

    @SuppressLint("SetTextI18n")
    public void onClickNumber(View view) { //Чисовые клавиши (от 0 до 9)
        Button button = (Button) view;
        if (calculator.operation == null) {
            calculator.number1 = (calculator.number1 == null) ? (button.getText().toString()) : (calculator.number1 + button.getText().toString());
            editTextInputResult.setText(calculator.number1);
        } else {
            calculator.number2 = (calculator.number2 == null) ? (button.getText().toString()) : (calculator.number2 + button.getText().toString());
            editTextInputResult.setText(calculator.number1 + " " + calculator.operation + " " + calculator.number2);
        }
    }

    @SuppressLint("SetTextI18n")
    public void onClickPoint(View view) { //разделитель (дробная часть)
        Button button = (Button) view;
        if (calculator.number1 != null && calculator.number2 == null && !calculator.number1.contains(button.getText().toString())) {
            calculator.number1 += button.getText().toString();
            editTextInputResult.setText(calculator.number1);
        }
        if (calculator.number2 != null && !calculator.number2.contains(button.getText().toString())) {
            calculator.number2 += button.getText().toString();
            editTextInputResult.setText(calculator.number1 + " " + calculator.operation + " " + calculator.number2);
        }
    }

    @SuppressLint("SetTextI18n")
    public void onClickCalcResult(View view) { //Кнопка равно
        Button button = (Button) view;
        try {
            if (calculator.number1 != null && calculator.number2 != null && calculator.operation != null) {
                Double result = calculator.getResult();
                calculator.number1 = result.toString();
                editTextInputResult.setText(calculator.number1);
                calculator.number2 = null;
                calculator.operation = null;
                if (!button.getText().toString().equals("=")) {
                    calculator.operation = button.getText().toString();
                    editTextInputResult.setText(calculator.number1 + " " + calculator.operation);
                }
            }
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickCalcPercent(View view) { //Вычисление процента
        try {
            if (calculator.number1 != null && calculator.number2 != null && calculator.operation != null) {
                Double result = calculator.calcPercent();
                calculator.number1 = result.toString();
                calculator.number2 = null;
                calculator.operation = null;
                editTextInputResult.setText(calculator.number1);
            }
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @SuppressLint("SetTextI18n")
    public void onClickPlusMinis(View view) { //Смена знака на проивоположный
        if (calculator.number1 != null && calculator.number2 == null) {
            if (calculator.number1.charAt(0) == '-') {
                calculator.number1 = calculator.number1.substring(1);
            } else {
                calculator.number1 = "-" + calculator.number1;
            }
            editTextInputResult.setText(calculator.number1);
        }
        if (calculator.number1 != null && calculator.number2 != null) {
            if (calculator.number2.charAt(0) == '-') {
                calculator.number2 = calculator.number2.substring(1);
            } else {
                calculator.number2 = "-" + calculator.number2;
            }
            editTextInputResult.setText(calculator.number1 + " " + calculator.operation + " " + calculator.number2);
        }
    }
}
