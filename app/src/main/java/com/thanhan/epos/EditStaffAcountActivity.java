package com.thanhan.epos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditStaffAcountActivity extends AppCompatActivity {
    private EditText id, name, gender, email, address, mobile, username, password;
    private Button update, cancel, delete;
    private String i_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_staff_acount);

        matching();

        getStaffAcountListDetail();

        update.setOnClickListener(new View.OnClickListener() {
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

                    reference.child(sid).child("name").setValue(sname);
                    reference.child(sid).child("address").setValue(saddress);
                    reference.child(sid).child("gender").setValue(sgender);
                    reference.child(sid).child("mobile").setValue(smobile);
                    reference.child(sid).child("email").setValue(semail);
                    reference.child(sid).child("username").setValue(susername);
                    reference.child(sid).child("matkhau").setValue(spassword);
                    Toast.makeText(getApplicationContext(), "Cập nhật thành công tài khoản" + sid, Toast.LENGTH_LONG).show();
                    finish();
                }catch (Exception e){
                    Log.d("Error update",e.toString());
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("NhanVien");

                reference.child(String.valueOf(i_id)).removeValue();
                Toast.makeText(getApplicationContext(),"Xóa thành công tài khoản",Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }

    private void getStaffAcountListDetail() {
        Intent intent = getIntent();
        i_id = intent.getStringExtra("ID");
        id.setText(String.valueOf(i_id));
        id.setEnabled(false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("NhanVien");

        reference.child(i_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                    name.setText(hashMap.get("name").toString());
                    gender.setText(hashMap.get("gender").toString());
                    email.setText(hashMap.get("email").toString());
                    address.setText(hashMap.get("address").toString());
                    mobile.setText(hashMap.get("mobile").toString());
                    username.setText(hashMap.get("username").toString());
                    password.setText(hashMap.get("matkhau").toString());
                } catch (Exception e) {
                    Log.d("Loi_json", e.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Loi_detail",error.toString());
            }
        });


    }

    private void matching() {
        id = (EditText) findViewById(R.id.et_editAcc_id);
        name = (EditText) findViewById(R.id.et_editAcc_name);
        gender = (EditText) findViewById(R.id.et_editAcc_gender);
        email = (EditText) findViewById(R.id.et_editAcc_email);
        address = (EditText) findViewById(R.id.et_editAcc_address);
        mobile = (EditText) findViewById(R.id.et_editAcc_mobile);
        username = (EditText) findViewById(R.id.et_editAcc_username);
        password = (EditText) findViewById(R.id.et_editAcc_password);
        update = (Button) findViewById(R.id.btn_editAcc_update);
        delete = (Button) findViewById(R.id.btn_editAcc_delete);
        cancel = (Button) findViewById(R.id.btn_editAcc_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}