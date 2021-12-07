package com.thanhan.epos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView username =(TextView) findViewById(R.id.username);
        TextView password =(TextView) findViewById(R.id.password);

        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);

        //admin and admin

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("vanhung") && password.getText().toString().equals("ad12345")){
                    //correct
                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công !!! ",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, Admin_MainActivity.class);
                    startActivity(intent);
                }else if (username.getText().toString().equals("thanhan") && password.getText().toString().equals("ad12345")){

                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công !!! ",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, Admin_MainActivity.class);
                    startActivity(intent);

                }else if (username.getText().toString().equals("trinhphu") && password.getText().toString().equals("ad12345")){
                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công !!! ",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, Admin_MainActivity.class);
                    startActivity(intent);

                }else if (username.getText().toString().equals("thanhan") && password.getText().toString().equals("987654321")){

                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công !!! ",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, Staff_MainActivity.class);
                    startActivity(intent);

                }else if (username.getText().toString().equals("chauly") && password.getText().toString().equals("123456")){

                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công !!! ",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, Staff_MainActivity.class);
                    startActivity(intent);

                }else if (username.getText().toString().equals("vanthang") && password.getText().toString().equals("hhhhh")){

                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công !!! ",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, Staff_MainActivity.class);
                    startActivity(intent);

                }else if (username.getText().toString().equals("thanhhoai") && password.getText().toString().equals("123456")){

                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công !!! ",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, Staff_MainActivity.class);
                    startActivity(intent);

                }
                else if (username.getText().toString().equals("vanhung") && password.getText().toString().equals("123456")){

                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công !!! ",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, Staff_MainActivity.class);
                    startActivity(intent);

                }else if (username.getText().toString().equals("ngocphu") && password.getText().toString().equals("123456")){

                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công !!! ",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, Staff_MainActivity.class);
                    startActivity(intent);

                }else if (username.getText().toString().equals("thanhtung") && password.getText().toString().equals("123456")){

                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công !!! ",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, Staff_MainActivity.class);
                    startActivity(intent);

                }else Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}