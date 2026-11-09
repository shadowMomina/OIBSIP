package com.hana.calculatorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplay, tvExpression;

    private String input = "0";      // number currently shown
    private double firstOperand = 0; // left side of the operation
    private String pendingOp = null; // + - × ÷ waiting to be applied
    private boolean startNew = false; // next digit starts a fresh number
    private boolean error = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);
        tvExpression = findViewById(R.id.tvExpression);

        int[] digitIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        for (int id : digitIds) {
            findViewById(id).setOnClickListener(v -> onDigit(((Button) v).getText().toString()));
        }

        int[] opIds = {R.id.btnAdd, R.id.btnSub, R.id.btnMul, R.id.btnDiv};
        for (int id : opIds) {
            findViewById(id).setOnClickListener(v -> onOperator(((Button) v).getText().toString()));
        }

        findViewById(R.id.btnDot).setOnClickListener(v -> onDot());
        findViewById(R.id.btnEq).setOnClickListener(v -> onEquals());
        findViewById(R.id.btnClear).setOnClickListener(v -> clearAll());
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackspace());
    }

    private void onDigit(String d) {
        if (error) clearAll();
        if (startNew) {
            input = "0";
            startNew = false;
        }
        if (input.length() >= 15) return;
        if (input.equals("0")) {
            input = d;
        } else {
            input += d;
        }
        updateDisplay();
    }

    private void onDot() {
        if (error) clearAll();
        if (startNew) {
            input = "0";
            startNew = false;
        }
        if (!input.contains(".")) {
            input += ".";
        }
        updateDisplay();
    }

    private void onOperator(String op) {
        if (error) return;
        if (pendingOp != null && !startNew) {
            // chaining: finish the previous operation first
            double result = calculate(firstOperand, parse(input), pendingOp);
            if (Double.isNaN(result)) {
                showError();
                return;
            }
            firstOperand = result;
            input = format(result);
        } else if (pendingOp == null) {
            firstOperand = parse(input);
        }
        pendingOp = op;
        startNew = true;
        tvExpression.setText(format(firstOperand) + " " + op);
        updateDisplay();
    }

    private void onEquals() {
        if (error || pendingOp == null) return;
        double second = parse(input);
        double result = calculate(firstOperand, second, pendingOp);
        if (Double.isNaN(result)) {
            showError();
            return;
        }
        tvExpression.setText(format(firstOperand) + " " + pendingOp + " " + format(second) + " =");
        input = format(result);
        pendingOp = null;
        startNew = true;
        updateDisplay();
    }

    private void onBackspace() {
        if (error) {
            clearAll();
            return;
        }
        if (startNew) return; // don't edit a finished result
        if (input.length() > 1) {
            input = input.substring(0, input.length() - 1);
            if (input.equals("-")) input = "0";
        } else {
            input = "0";
        }
        updateDisplay();
    }

    private void clearAll() {
        input = "0";
        firstOperand = 0;
        pendingOp = null;
        startNew = false;
        error = false;
        tvExpression.setText("");
        updateDisplay();
    }

    private void showError() {
        error = true;
        pendingOp = null;
        startNew = true;
        input = "0";
        firstOperand = 0;
        tvExpression.setText("");
        tvDisplay.setText("Error");
    }

    // returns NaN when the operation is invalid (division by zero)
    private double calculate(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "−": return a - b;
            case "×": return a * b;
            case "÷": return b == 0 ? Double.NaN : a / b;
            default:  return b;
        }
    }

    private double parse(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String format(double v) {
        BigDecimal bd = BigDecimal.valueOf(v).setScale(10, RoundingMode.HALF_UP).stripTrailingZeros();
        String s = bd.toPlainString();
        return s.equals("-0") ? "0" : s;
    }

    private void updateDisplay() {
        tvDisplay.setText(input);
    }
}