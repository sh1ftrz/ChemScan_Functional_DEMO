package com.example.chemapp30;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chemapp30.database.AppDatabase;
import com.example.chemapp30.database.Chemical;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddChemicalActivity extends AppCompatActivity {

    private EditText edtCode, edtName, edtInfo, edtProperty, edtFirstAid;
    private Button btnSave;
    private final ExecutorService io = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_chemical);

        findViewById(R.id.homeBTNAC).setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        edtCode = findViewById(R.id.edtCode);
        edtName = findViewById(R.id.edtName);
        edtInfo = findViewById(R.id.edtInfo);
        edtProperty = findViewById(R.id.edtProperty);
        edtFirstAid = findViewById(R.id.edtFirstAid);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> save());
    }

    private void save() {
        final String code = edtCode.getText().toString().trim();
        final String name = edtName.getText().toString().trim();
        final String info = safeText(edtInfo);
        final String property = safeText(edtProperty);
        final String firstAid = safeText(edtFirstAid);

        if (code.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "กรุณากรอกรหัสและชื่อสาร", Toast.LENGTH_SHORT).show();
            return;
        }

        io.execute(() -> {
            try {
                Chemical c = new Chemical();
                c.setCode(code);
                c.setName(name);
                c.setInfo(info);
                c.setProperty(property);
                c.setFirstAid(firstAid);

                AppDatabase.getInstance(getApplicationContext())
                        .chemicalDao()
                        .insert(c);

                runOnUiThread(() -> {
                    Toast.makeText(this, "บันทึกสารสำเร็จ", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception e) {
                final String msg = e.getMessage();
                runOnUiThread(() ->
                        Toast.makeText(this, "บันทึกไม่สำเร็จ: " + msg, Toast.LENGTH_LONG).show());
            }
        });
    }

    private String safeText(EditText et) {
        String s = et.getText() == null ? null : et.getText().toString().trim();
        return (s == null || s.isEmpty()) ? null : s;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        io.shutdown();
    }
}
