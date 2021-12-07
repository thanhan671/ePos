package com.thanhan.epos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.thanhan.epos.ExportData.ExportBill_MainActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Admin_MainActivity extends AppCompatActivity {
    TextView logout;
    private Button btnLoaiHang, btnHangHoa, btnAC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);


        findViewById(R.id.btn_admin_nhaphang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_MainActivity.this, ImportBill_MainActivity.class);
        matching();
        logout = findViewById(R.id.textView2);
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Admin_MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(Admin_MainActivity.this, "ĐĂNG XUẤT THÀNH CÔNG", Toast.LENGTH_SHORT).show();
            }
        });

        btnLoaiHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_MainActivity.this, AddProductTypeActivity.class);
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

        btnHangHoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_MainActivity.this, ViewListProductActivity.class);
                startActivity(intent);
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