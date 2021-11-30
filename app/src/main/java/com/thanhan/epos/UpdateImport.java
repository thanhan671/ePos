package com.thanhan.epos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UpdateImport extends AppCompatActivity {

    Button capnhat, xoa, huy;
    EditText code, tenhang, soluong, ngaynhap, ngaysua, thanhtien;
    String i_id;
    TextView sophieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_import);

        matching();

        getContactDetail();
    }
    private void getContactDetail() {
        Intent intent = getIntent();
        i_id = intent.getStringExtra("ID");
        sophieu.setText("Phiếu nhập số: "+String.valueOf(i_id));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("PhieuNhap");
        reference.child(String.valueOf(i_id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    code.setText(hashMap.get("maCode").toString());
                    tenhang.setText(hashMap.get("tenHang").toString());
                    soluong.setText(hashMap.get("soLuong").toString());
                    ngaynhap.setText(hashMap.get("ngayNhap").toString());
                    ngaysua.setText(hashMap.get("ngayThayDoi").toString());
                    thanhtien.setText(hashMap.get("thanhTien").toString());
                } catch (Exception e) {
                    Log.d("Loi_json",e.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Loi_detail", databaseError.toString());
            }
        });
    }
    private void matching() {
        capnhat = findViewById(R.id.btn_nhap_capnhat);
        xoa = findViewById(R.id.btn_nhap_xoa);
        huy = findViewById(R.id.btn_nhap_huy);
        sophieu = findViewById(R.id.tv_phieunhap);
        code = findViewById(R.id.edt_nhap_code);
        tenhang = findViewById(R.id.edt_nhap_tenhang);
        soluong = findViewById(R.id.edt_nhap_soluong);
        ngaynhap = findViewById(R.id.edt_nhap_ngaynhap);
        ngaysua = findViewById(R.id.edt_nhap_ngaysua);
        thanhtien = findViewById(R.id.edt_nhap_thanhtien);
    }
}