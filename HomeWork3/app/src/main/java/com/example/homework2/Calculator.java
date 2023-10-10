package com.example.homework2;

public class Calculator {
    public String number1;
    public String number2;
    public String operation;

    public void defaultValue() {
        number1 = null;
        number2 = null;
        operation = null;
    }

    public double calcPercent() throws IllegalArgumentException {
        Double result = 0d;
        switch (operation) {
            case "+":
                result = Double.parseDouble(number1) + ((Double.parseDouble(number1) * Double.parseDouble(number2)) / 100);
                break;
            case "-":
                result = Double.parseDouble(number1) - ((Double.parseDouble(number1) * Double.parseDouble(number2)) / 100);
                break;
            case "*":
                result = Double.parseDouble(number1) * ((Double.parseDouble(number1) * Double.parseDouble(number2)) / 100);
                break;
            case "/":
                result = Double.parseDouble(number1) / ((Double.parseDouble(number1) * Double.parseDouble(number2)) / 100);
                break;
        }
        if (result > Double.MAX_VALUE) {
            throw new IllegalArgumentException("Выход за рамки используемого типа данных");
        }
        return result;
    }

    public double getResult() throws IllegalArgumentException {
        Double result = 0d;
        switch (operation) {
            case "+":
                result = Double.parseDouble(number1) + Double.parseDouble(number2);
                break;
            case "-":
                result = Double.parseDouble(number1) - Double.parseDouble(number2);
                break;
            case "*":
                result = Double.parseDouble(number1) * Double.parseDouble(number2);
                break;
            case "/":
                result = Double.parseDouble(number1) / Double.parseDouble(number2);
                break;
        }
        if (result > Double.MAX_VALUE) {
            throw new IllegalArgumentException("Выход за рамки используемого типа данных");
        }
        return result;
    }
}
