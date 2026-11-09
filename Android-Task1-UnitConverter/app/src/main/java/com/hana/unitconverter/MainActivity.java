package com.hana.unitconverter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    private final String[] categories = {"Length", "Weight", "Temperature"};

    private final String[][] units = {
            {"Millimetre", "Centimetre", "Metre", "Kilometre", "Inch", "Foot", "Yard", "Mile"},
            {"Milligram", "Gram", "Kilogram", "Pound", "Ounce"},
            {"Celsius", "Fahrenheit", "Kelvin"}
    };

    // factor to convert each unit into the base unit (metre for length, gram for weight)
    private final double[][] factors = {
            {0.001, 0.01, 1, 1000, 0.0254, 0.3048, 0.9144, 1609.344},
            {0.001, 1, 1000, 453.592, 28.3495},
            {}
    };

    private Spinner spCategory, spFrom, spTo;
    private EditText etValue;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spCategory = findViewById(R.id.spCategory);
        spFrom = findViewById(R.id.spFrom);
        spTo = findViewById(R.id.spTo);
        etValue = findViewById(R.id.etValue);
        tvResult = findViewById(R.id.tvResult);
        Button btnConvert = findViewById(R.id.btnConvert);

        spCategory.setAdapter(makeAdapter(categories));

        // changing category resets both unit dropdowns
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadUnits(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        loadUnits(0);

        btnConvert.setOnClickListener(v -> convert());
    }

    private ArrayAdapter<String> makeAdapter(String[] items) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void loadUnits(int category) {
        spFrom.setAdapter(makeAdapter(units[category]));
        spTo.setAdapter(makeAdapter(units[category]));
        spTo.setSelection(1); // different default so the first result is not the same unit
        tvResult.setText("Result will appear here");
    }

    private void convert() {
        String text = etValue.getText().toString().trim();

        if (text.isEmpty()) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show();
            return;
        }

        double value;
        try {
            value = Double.parseDouble(text);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        int category = spCategory.getSelectedItemPosition();
        int from = spFrom.getSelectedItemPosition();
        int to = spTo.getSelectedItemPosition();
        double result;

        if (category == 2) {
            double celsius = toCelsius(value, from);
            if (celsius < -273.15) {
                Toast.makeText(this, "Below absolute zero is not possible", Toast.LENGTH_SHORT).show();
                return;
            }
            result = fromCelsius(celsius, to);
        } else {
            double base = value * factors[category][from];
            result = base / factors[category][to];
        }

        tvResult.setText(format(value) + " " + units[category][from]
                + " = " + format(result) + " " + units[category][to]);
    }

    private double toCelsius(double v, int unit) {
        switch (unit) {
            case 1: return (v - 32) * 5 / 9; // Fahrenheit
            case 2: return v - 273.15;       // Kelvin
            default: return v;               // Celsius
        }
    }

    private double fromCelsius(double c, int unit) {
        switch (unit) {
            case 1: return c * 9 / 5 + 32;
            case 2: return c + 273.15;
            default: return c;
        }
    }

    private String format(double v) {
        return BigDecimal.valueOf(v).setScale(4, RoundingMode.HALF_UP)
                .stripTrailingZeros().toPlainString();
    }
}