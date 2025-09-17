package com.example.chemapp30;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chemapp30.database.AppDatabase;
import com.example.chemapp30.database.DangerousMix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddDangerousMixActivity extends AppCompatActivity {

    private EditText edtCode1, edtCode2, edtWarning;
    private Button btnSave;
    private final ExecutorService io = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_dangerous_mix);

        findViewById(R.id.homeBTNADM).setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        edtCode1 = findViewById(R.id.edtCode1);
        edtCode2 = findViewById(R.id.edtCode2);
        edtWarning = findViewById(R.id.edtWarning);
        btnSave = findViewById(R.id.btnSaveMix);

        btnSave.setOnClickListener(v -> save());
    }

    private void save() {
        final String a = edtCode1.getText().toString().trim();
        final String b = edtCode2.getText().toString().trim();
        final String warn = edtWarning.getText() == null ? null : edtWarning.getText().toString().trim();

        if (a.isEmpty() || b.isEmpty()) {
            Toast.makeText(this, "กรุณากรอกรหัสสารให้ครบ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (a.equalsIgnoreCase(b)) {
            Toast.makeText(this, "ห้ามเลือกสารชนิดเดียวกันทั้งสองช่อง", Toast.LENGTH_SHORT).show();
            return;
        }

        io.execute(() -> {
            try {
                DangerousMix m = new DangerousMix();
                m.setCode1(a);
                m.setCode2(b);
                m.setWarning((warn == null || warn.isEmpty()) ? "ห้ามผสม อันตราย!" : warn);

                AppDatabase.getInstance(getApplicationContext())
                        .dangerousMixDao()
                        .insert(m);

                runOnUiThread(() -> {
                    Toast.makeText(this, "บันทึกคู่สารอันตรายสำเร็จ", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception e) {
                final String msg = e.getMessage();
                runOnUiThread(() ->
                        Toast.makeText(this, "บันทึกไม่สำเร็จ: " + msg, Toast.LENGTH_LONG).show());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        io.shutdown();
    }
}
