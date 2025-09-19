package com.example.chemapp30;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class CalculationActivity extends AppCompatActivity {

    private EditText edtInput1, edtInput2;
    private Button btnMol, btnWW, btnWV, btnVV;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculation);

        findViewById(R.id.homeBTNCalc).setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        edtInput1 = findViewById(R.id.edtInput1);
        edtInput2 = findViewById(R.id.edtInput2);

        btnMol = findViewById(R.id.btnMol);

        btnWW = findViewById(R.id.btnWW);
        btnWV = findViewById(R.id.btnWV);
        btnVV = findViewById(R.id.btnVV);

        btnMol.setOnClickListener(v -> calculateMol());

        btnWW.setOnClickListener(v -> calculateWW());
        btnWV.setOnClickListener(v -> calculateWV());
        btnVV.setOnClickListener(v -> calculateVV());
    }

    private void calculateMol() {
        try {
            double solute = parseDouble(edtInput1);
            double volume = parseDouble(edtInput2);
            if (volume <= 0) {
                Toast.makeText(this, "ปริมาตรต้องมากกว่า 0", Toast.LENGTH_SHORT).show();
                return;
            }
            double wv = solute / volume;
            Toast.makeText(this, String.format("Mol = %.2f Mol", wv), Toast.LENGTH_LONG).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "กรุณากรอกตัวเลขให้ถูกต้อง", Toast.LENGTH_SHORT).show();
        }
    }
    private void calculateWW() {
        try {
            double solute = parseDouble(edtInput1);
            double volume = parseDouble(edtInput2);
            if (volume <= 0) {
                Toast.makeText(this, "ปริมาตรต้องมากกว่า 0", Toast.LENGTH_SHORT).show();
                return;
            }
            double wv = solute / volume * 100.0;
            Toast.makeText(this, String.format("w/w = %.2f%%", wv), Toast.LENGTH_LONG).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "กรุณากรอกตัวเลขให้ถูกต้อง", Toast.LENGTH_SHORT).show();
        }
    }
    private void calculateWV() {
        try {
            double solute = parseDouble(edtInput1);
            double volume = parseDouble(edtInput2);
            if (volume <= 0) {
                Toast.makeText(this, "ปริมาตรต้องมากกว่า 0", Toast.LENGTH_SHORT).show();
                return;
            }
            double wv = solute / volume * 100.0;
            Toast.makeText(this, String.format("w/v = %.2f%%", wv), Toast.LENGTH_LONG).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "กรุณากรอกตัวเลขให้ถูกต้อง", Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateVV() {
        try {
            double volSolute = parseDouble(edtInput1);
            double volSolution = parseDouble(edtInput2);
            if (volSolution <= 0) {
                Toast.makeText(this, "ปริมาตรต้องมากกว่า 0", Toast.LENGTH_SHORT).show();
                return;
            }
            double vv = volSolute / volSolution * 100.0;
            Toast.makeText(this, String.format("v/v = %.2f%%", vv), Toast.LENGTH_LONG).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "กรุณากรอกตัวเลขให้ถูกต้อง", Toast.LENGTH_SHORT).show();
        }
    }

    private double parseDouble(EditText et) throws NumberFormatException {
        String s = et.getText() == null ? "" : et.getText().toString().trim();
        return Double.parseDouble(s);
    }
}
