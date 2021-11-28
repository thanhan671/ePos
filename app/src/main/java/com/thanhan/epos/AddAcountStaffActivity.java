package com.thanhan.epos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddAcountStaffActivity extends AppCompatActivity {
    private EditText id, name, gender, email, address, mobile, username, password;
    private Button add, cancel;
    private int i_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_acount_staff);

        matching();

        Intent intent = getIntent();
        i_id = intent.getIntExtra("ID", -1);
        id.setEnabled(false);
        id.setText(String.valueOf(i_id));


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String sid = id.getText().toString().trim();
                    String sname = name.getText().toString().trim();
                    String saddress = address.getText().toString().trim();
                    String sgender = gender.getText().toString().trim();
                    String smobile = mobile.getText().toString().trim();
                    String semail = email.getText().toString().trim();
                    String susername = username.getText().toString().trim();
                    String spassword = password.getText().toString().trim();


                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("NhanVien");

                    if (sname.isEmpty() || saddress.isEmpty() || sgender.isEmpty() || smobile.isEmpty() || semail.isEmpty() ||
                            susername.isEmpty() || spassword.isEmpty()) {
                        Toast.makeText(AddAcountStaffActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    } else {
                        reference.child(sid).child("name").setValue(sname);
                        reference.child(sid).child("address").setValue(saddress);
                        reference.child(sid).child("gender").setValue(sgender);
                        reference.child(sid).child("mobile").setValue(smobile);
                        reference.child(sid).child("email").setValue(semail);
                        reference.child(sid).child("username").setValue(susername);
                        reference.child(sid).child("matkhau").setValue(spassword);
                        Toast.makeText(getApplicationContext(), "Thêm thành công tài khoản" + sid, Toast.LENGTH_LONG).show();
                        finish();
                    }

                } catch (Exception e) {
                    Log.d("Error add", e.toString());
                }
            }
        });
    }

    private void matching() {

        id = (EditText) findViewById(R.id.et_addAcc_id);
        name = (EditText) findViewById(R.id.et_addAcc_name);
        gender = (EditText) findViewById(R.id.et_addAcc_gender);
        email = (EditText) findViewById(R.id.et_addAcc_email);
        address = (EditText) findViewById(R.id.et_addAcc_adress);
        mobile = (EditText) findViewById(R.id.et_addAcc_mobile);
        username = (EditText) findViewById(R.id.et_addAcc_username);
        password = (EditText) findViewById(R.id.et_addAcc_password);
        add = (Button) findViewById(R.id.btn_addAcc_add);
        cancel = (Button) findViewById(R.id.btn_addAcc_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}