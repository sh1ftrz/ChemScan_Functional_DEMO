package com.example.chemapp30;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chemapp30.database.AppDatabase;
import com.example.chemapp30.database.Chemical;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChemicalDetailActivity extends AppCompatActivity {

    private TextView txtCode, txtName, txtInfo, txtProperty, txtFirstAid;
    private final ExecutorService io = Executors.newSingleThreadExecutor();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chemical_detail);

        findViewById(R.id.backBTNCD).setOnClickListener(v ->
                startActivity(new Intent(this, ScanOrSearchActivity.class)));

        txtCode = findViewById(R.id.txtCode);
        txtName = findViewById(R.id.txtName);
        txtInfo = findViewById(R.id.txtInfo);
        txtProperty = findViewById(R.id.txtProperty);
        txtFirstAid = findViewById(R.id.txtFirstAid);

        String code = getIntent().getStringExtra(ScanOrSearchActivity.EXTRA_CODE);
        if (code == null || code.trim().isEmpty()) {
            Toast.makeText(this, "ไม่มีรหัสสาร", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadChemical(code.trim());
    }

    private void loadChemical(String code) {
        io.execute(() -> {
            Chemical chem = null;
            try {
                chem = AppDatabase.getInstance(getApplicationContext())
                        .chemicalDao()
                        .getChemicalByCode(code);
            } catch (Exception e) {
                final String msg = e.getMessage();
                runOnUiThread(() ->
                        Toast.makeText(this, "ผิดพลาด: " + msg, Toast.LENGTH_LONG).show());
            }

            final Chemical c = chem;
            runOnUiThread(() -> {
                if (c == null) {
                    Toast.makeText(this, "ไม่พบข้อมูลสาร", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    if (txtCode != null) txtCode.setText(safe(c.getCode()));
                    if (txtName != null) txtName.setText(safe(c.getName()));
                    if (txtInfo != null) txtInfo.setText("ข้อมูล: " + safe(c.getInfo()));
                    if (txtProperty != null) txtProperty.setText("คุณสมบัติ: " + safe(c.getProperty()));
                    if (txtFirstAid != null) txtFirstAid.setText("ปฐมพยาบาล: " + safe(c.getFirstAid()));
                }
            });
        });
    }

    private String safe(String s) { return s == null ? "-" : s; }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        io.shutdown();
    }
}
