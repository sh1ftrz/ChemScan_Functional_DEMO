package com.example.chemapp30;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.chemapp30.database.AppDatabase;
import com.example.chemapp30.database.Chemical;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanOrSearchActivity extends AppCompatActivity {

    public static final String EXTRA_CODE = "code";

    private EditText edtCode;
    private Button btnSearch, btnScan;
    private final ExecutorService io = Executors.newSingleThreadExecutor();

    // 1) ตัวขอสแกน ZXing (Activity Result API)
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() != null) {
                    String scanned = result.getContents().trim();
                    edtCode.setText(scanned);
                    doSearch(); // สแกนได้แล้วค้นหาเลย
                }
            });

    // 2) ขอสิทธิ์กล้องตอนรันไทม์ (Android 6+)
    private final ActivityResultLauncher<String> requestCameraPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    startScan();
                } else {
                    Toast.makeText(this, "ต้องอนุญาตสิทธิ์กล้องเพื่อสแกน", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scan_or_search);

        findViewById(R.id.homeBTNSorS).setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        edtCode = findViewById(R.id.edtCode);
        btnSearch = findViewById(R.id.btnSearch);
        btnScan = findViewById(R.id.btnScan);

        btnSearch.setOnClickListener(v -> doSearch());

        // เปลี่ยนให้ปุ่มสแกนเรียก startScan() (มีเช็ค permission)
        btnScan.setOnClickListener(v -> {
            if (hasCameraPermission()) {
                startScan();
            } else {
                requestCameraPermission.launch(Manifest.permission.CAMERA);
            }
        });
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    // 3) ฟังก์ชันเริ่มสแกน (กำหนดให้รับเฉพาะ QR Code)
    private void startScan() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE); // เน้น QR
        options.setPrompt("เล็งกล้องไปที่ QR Code");
        options.setCameraId(0); // กล้องหลัง
        options.setBeepEnabled(true);
        options.setBarcodeImageEnabled(false);
        options.setOrientationLocked(false); // ให้หมุนจอได้
        // ถ้าอยากสแกนบาร์โค้ดชนิดอื่นด้วย ให้ใช้ .setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)

        barcodeLauncher.launch(options);
    }

    private void doSearch() {
        final String code = edtCode.getText().toString().trim();
        if (code.isEmpty()) {
            Toast.makeText(this, "กรุณากรอกรหัสสารเคมี", Toast.LENGTH_SHORT).show();
            return;
        }

        io.execute(() -> {
            try {
                Chemical chem = AppDatabase.getInstance(getApplicationContext())
                        .chemicalDao()
                        .getChemicalByCode(code);

                runOnUiThread(() -> {
                    if (chem != null) {
                        Intent i = new Intent(this, ChemicalDetailActivity.class);
                        i.putExtra(EXTRA_CODE, code);
                        startActivity(i);
                    } else {
                        Toast.makeText(this, "ไม่พบสาร", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "เกิดข้อผิดพลาด: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        io.shutdown();
    }
}
