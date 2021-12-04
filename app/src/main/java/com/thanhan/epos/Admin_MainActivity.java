package com.thanhan.epos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.thanhan.epos.ExportData.ExportBill_MainActivity;

public class Admin_MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        findViewById(R.id.btn_admin_nhaphang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_MainActivity.this, ImportBill_MainActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_admin_xuathang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentXuatHang = new Intent(Admin_MainActivity.this, ExportBill_MainActivity.class);
                startActivity(intentXuatHang);
            }
        });
    }
}