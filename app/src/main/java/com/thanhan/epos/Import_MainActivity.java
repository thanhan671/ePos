package com.thanhan.epos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.IpPrefix;
import android.os.Bundle;
import android.view.View;

public class Import_MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_main);

        findViewById(R.id.btn_quetnhap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent
                        (Import_MainActivity.this, ScanProductCode.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_phieunhap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phieunhap = new Intent
                        (Import_MainActivity.this, ListImport.class);
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