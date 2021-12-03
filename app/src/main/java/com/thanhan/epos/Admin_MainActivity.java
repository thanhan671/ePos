package com.thanhan.epos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Admin_MainActivity extends AppCompatActivity {
    private Button btnLoaiHang, btnHangHoa, btnAC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        matching();

        btnLoaiHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_MainActivity.this, AddProductTypeActivity.class);
                startActivity(intent);
            }
        });

        btnHangHoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin_MainActivity.this, StaffAcountActivity.class);
                startActivity(intent);
            }
        });
    }

    private void matching() {
        btnLoaiHang = (Button) findViewById(R.id.danhmuc);
        btnHangHoa = (Button) findViewById(R.id.hanghoa);
        btnAC = (Button) findViewById(R.id.taikhoan);
    }
}