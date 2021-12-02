package com.thanhan.epos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ImportBill_MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_bill_main);

        findViewById(R.id.btn_quetnhap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent
                        (ImportBill_MainActivity.this, ScanProductCode.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_phieunhap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phieunhap = new Intent
                        (ImportBill_MainActivity.this, ListImport.class);
                startActivity(phieunhap);
            }
        });
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}