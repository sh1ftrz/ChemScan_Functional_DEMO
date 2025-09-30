package com.example.chemapp30;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanOrSearchActivity extends AppCompatActivity {

    public static final String EXTRA_CODE = "code";

    private EditText edtCode;
    private Button btnSearch, btnScanQR, btnScanBarcode; // <- เพิ่มปุ่มแยก
    private final ExecutorService io = Executors.newSingleThreadExecutor();

    // โหมดที่จะสแกนหลังจากได้สิทธิ์กล้อง
    private enum ScanMode { QR_ONLY, BARCODE_1D_COMMON }
    private ScanMode pendingMode = null;

    // 1) ZXing (Activity Result API)
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() != null) {
                    String scanned = result.getContents().trim();
                    String format = result.getFormatName(); // เช่น QR_CODE, EAN_13, CODE_128
                    edtCode.setText(scanned);
                    Toast.makeText(this, "สแกนสำเร็จ (" + format + "): " + scanned, Toast.LENGTH_SHORT).show();
                    doSearch(); // สแกนได้แล้วค้นหาเลย
                }
            });

    // 2) ขอสิทธิ์กล้องตอนรันไทม์
    private final ActivityResultLauncher<String> requestCameraPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    // กลับไปสแกนตามโหมดที่ผู้ใช้กดปุ่มไว้
                    if (pendingMode != null) {
                        startScan(pendingMode);
                    }
                } else {
                    Toast.makeText(this, "ต้องอนุญาตสิทธิ์กล้องเพื่อสแกน", Toast.LENGTH_SHORT).show();
                }
            });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scan_or_search);

        findViewById(R.id.homeBTNSorS).setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        edtCode = findViewById(R.id.edtCode);
        btnSearch = findViewById(R.id.btnSearch);

        // เปลี่ยนจากปุ่มเดิม R.id.btnScan -> ใช้เป็น "สแกน QR"
        btnScanQR = findViewById(R.id.btnScan);
        // ปุ่มใหม่ "สแกนบาร์โค้ด"
        btnScanBarcode = findViewById(R.id.btnScanBarcode);

        btnSearch.setOnClickListener(v -> doSearch());

        // ปุ่มสแกน QR
        btnScanQR.setOnClickListener(v -> {
            pendingMode = ScanMode.QR_ONLY;
            if (hasCameraPermission()) {
                startScan(pendingMode);
            } else {
                requestCameraPermission.launch(Manifest.permission.CAMERA);
            }
        });

        // ปุ่มสแกนบาร์โค้ด (1D ทั่วไป)
        btnScanBarcode.setOnClickListener(v -> {
            pendingMode = ScanMode.BARCODE_1D_COMMON;
            if (hasCameraPermission()) {
                startScan(pendingMode);
            } else {
                requestCameraPermission.launch(Manifest.permission.CAMERA);
            }
        });
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    // 3) ฟังก์ชันเริ่มสแกน แยกตามโหมด
    private void startScan(@NonNull ScanMode mode) {
        ScanOptions options = new ScanOptions();

        switch (mode) {
            case QR_ONLY:
                options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
                options.setPrompt("เล็งกล้องไปที่ QR Code");
                break;

            case BARCODE_1D_COMMON:
                // 1D ที่พบบ่อย (ปรับเพิ่ม/ลดได้ตามต้องการ)
                List<String> oneD = Arrays.asList(
                        ScanOptions.EAN_13,
                        ScanOptions.EAN_8,
                        ScanOptions.UPC_A,
                        ScanOptions.UPC_E,
                        ScanOptions.CODE_128,
                        ScanOptions.CODE_39,
                        ScanOptions.ITF
                );
                options.setDesiredBarcodeFormats(oneD);
                options.setPrompt("เล็งกล้องไปที่บาร์โค้ด (EAN/UPC/Code128 ฯลฯ)");
                break;
        }

        options.setCameraId(0);                 // กล้องหลัง
        options.setBeepEnabled(true);
        options.setBarcodeImageEnabled(false);
        options.setOrientationLocked(false);    // อนุญาตหมุนจอ

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
