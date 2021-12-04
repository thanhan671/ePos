package com.thanhan.epos.ExportData;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.thanhan.epos.R;

public class ExportBill_MainActivity extends AppCompatActivity {
    Button btn_phieuXuat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_bill_main);
        matching();
        btn_phieuXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phieuXuatntent = new Intent(ExportBill_MainActivity.this, ListExportActivity.class);
                startActivity(phieuXuatntent);
            }
        });

        findViewById(R.id.btn_goBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void matching() {
        btn_phieuXuat = (Button) findViewById(R.id.btn_phieuXuatHang);
    }
}