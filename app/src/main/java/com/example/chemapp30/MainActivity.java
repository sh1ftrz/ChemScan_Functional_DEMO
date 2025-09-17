package com.example.chemapp30;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnScan).setOnClickListener(v ->
                startActivity(new Intent(this, ScanOrSearchActivity.class)));

        findViewById(R.id.btnAddChem).setOnClickListener(v ->
                startActivity(new Intent(this, AddChemicalActivity.class)));

        findViewById(R.id.btnAddMix).setOnClickListener(v ->
                startActivity(new Intent(this, AddDangerousMixActivity.class)));

        findViewById(R.id.btnMix).setOnClickListener(v ->
                startActivity(new Intent(this, MixCheckActivity.class)));

        findViewById(R.id.btnCalc).setOnClickListener(v ->
                startActivity(new Intent(this, CalculationActivity.class)));
    }
}
