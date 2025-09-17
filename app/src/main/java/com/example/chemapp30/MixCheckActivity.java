package com.example.chemapp30;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chemapp30.database.AppDatabase;
import com.example.chemapp30.database.DangerousMix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MixCheckActivity extends AppCompatActivity {

    private EditText edtCode1, edtCode2;
    private TextView txtResult;
    private final ExecutorService io = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mix_check);

        findViewById(R.id.homeBTNMC).setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        edtCode1 = findViewById(R.id.edtCode1);
        edtCode2 = findViewById(R.id.edtCode2);
        txtResult = findViewById(R.id.txtResult);

        findViewById(R.id.btnCheck).setOnClickListener(v -> checkMix());
    }

    private void checkMix() {
        final String a = edtCode1.getText().toString().trim();
        final String b = edtCode2.getText().toString().trim();

        if (a.isEmpty() || b.isEmpty()) {
            Toast.makeText(this, "กรุณากรอกรหัสให้ครบทั้งสองช่อง", Toast.LENGTH_SHORT).show();
            return;
        }
        if (a.equalsIgnoreCase(b)) {
            Toast.makeText(this, "รหัสทั้งสองเหมือนกัน", Toast.LENGTH_SHORT).show();
            return;
        }

        io.execute(() -> {
            DangerousMix mix = null;
            try {
                mix = AppDatabase.getInstance(getApplicationContext())
                        .dangerousMixDao()
                        .getMix(a, b);
            } catch (Exception e) {
                final String msg = e.getMessage();
                runOnUiThread(() ->
                        Toast.makeText(this, "ผิดพลาด: " + msg, Toast.LENGTH_LONG).show());
            }

            final DangerousMix m = mix;
            runOnUiThread(() -> {
                if (m != null) {
                    String warn = (m.getWarning() == null || m.getWarning().isEmpty())
                            ? "พบเป็นคู่สารอันตราย ห้ามผสม!"
                            : m.getWarning();
                    txtResult.setText("⚠️ " + warn);
                } else {
                    txtResult.setText("✅ ไม่พบข้อมูลว่าเป็นคู่สารอันตราย");
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        io.shutdown();
    }
}
