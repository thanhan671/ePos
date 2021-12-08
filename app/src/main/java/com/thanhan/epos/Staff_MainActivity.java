package com.thanhan.epos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.appsearch.GetByDocumentIdRequest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.thanhan.epos.ExportData.ExportBill_MainActivity;

public class Staff_MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main);

        findViewById(R.id.tv_nv_dangxuat).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
                Toast.makeText(Staff_MainActivity.this, "ĐĂNG XUẤT THÀNH CÔNG", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_nv_nhaphang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ImportBill_MainActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_nv_xuathang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExportBill_MainActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_nv_thongke).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Statistic.class);
                startActivity(intent);
            }
        });
    }
}