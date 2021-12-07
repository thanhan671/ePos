package com.thanhan.epos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Staff_MainActivity extends AppCompatActivity {
    TextView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main);
        logout = findViewById(R.id.textView2);
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Staff_MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(Staff_MainActivity.this, "ĐĂNG XUẤT THÀNH CÔNG", Toast.LENGTH_SHORT).show();
            }
        });
    }
}