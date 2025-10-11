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


            //Mol
            double Massofsubtance = parseDouble(edtInput1);
            double Molarmass = parseDouble(edtInput2);


            if (Massofsubtance <= 0) {
                Toast.makeText(this, "มวลของสารต้องมากกว่า 0", Toast.LENGTH_SHORT).show()
                ;
                return;
            }

            if (Molarmass <= 0) {
                Toast.makeText(this, "มวลโมเลกุลต้องมากกว่า 0", Toast.LENGTH_SHORT).show()
                ;
                return;
            }

            //สูตรหาจำนวนMol
            double Numberofmoles = Massofsubtance / Molarmass;
            Toast.makeText(this, String.format("Numberofmoles = %.2f Mol", Numberofmoles), Toast.LENGTH_LONG).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "กรุณากรอกตัวเลขให้ถูกต้อง", Toast.LENGTH_SHORT).show();
        }
    }


    private void calculateWW() {
        try {


            double SoluteMass = parseDouble(edtInput1);
            double SolutionMass = parseDouble(edtInput2);


            if (SoluteMass <= 0) {
                Toast.makeText(this, "น้ำหนักสารตัวถูกละลายต้องมากกว่า 0", Toast.LENGTH_SHORT).show();
                return;
            }
            if (SolutionMass <= 0) {
                Toast.makeText(this, "น้ำหนักของสารละลายต้องมากกว่า 0", Toast.LENGTH_SHORT).show();
                return;
            }

            //สูตรหา%WW
            double WW = SoluteMass / SolutionMass * 100.0;
            Toast.makeText(this, String.format("w/w = %.2f%%", WW), Toast.LENGTH_LONG).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "กรุณากรอกตัวเลขให้ถูกต้อง", Toast.LENGTH_SHORT).show();
        }
    }


    private void calculateWV() {
        try {
            double SoluteMass = parseDouble(edtInput1);
            double SoluteVolume = parseDouble(edtInput2);


            if (SoluteMass <= 0) {
                Toast.makeText(this, "น้ำหนักสารตัวถูกละลายต้องมากกว่า 0", Toast.LENGTH_SHORT).show();
                return;
            }
            if (SoluteVolume <= 0) {
                Toast.makeText(this, "ปริมาตรของสารละลายต้องมากกว่า 0", Toast.LENGTH_SHORT).show();
                return;
            }


            double WV = SoluteMass / SoluteVolume * 100.0;
            Toast.makeText(this, String.format("w/v = %.2f%%", WV), Toast.LENGTH_LONG).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "กรุณากรอกตัวเลขให้ถูกต้อง", Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateVV() {
        try {
            double SoluteVolume = parseDouble(edtInput1);
            double SolutionVolume = parseDouble(edtInput2);


            if (SoluteVolume <= 0) {
                Toast.makeText(this, "ปริมาตรของตัวถูกต้องมากกว่า 0", Toast.LENGTH_SHORT).show();
                return;
            }

            if (SolutionVolume <= 0) {
                Toast.makeText(this, "ปริมาตรของสารละลายต้องมากกว่า 0", Toast.LENGTH_SHORT).show();
                return;


            }
            double VV = SoluteVolume / SolutionVolume * 100.0;
            Toast.makeText(this, String.format("v/v = %.2f%%", VV), Toast.LENGTH_LONG).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "กรุณากรอกตัวเลขให้ถูกต้อง", Toast.LENGTH_SHORT).show();
        }
    }

    private double parseDouble(EditText et) throws NumberFormatException {
        String s = et.getText() == null ? "" : et.getText().toString().trim();
        return Double.parseDouble(s);
    }
}
