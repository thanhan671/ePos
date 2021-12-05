package com.thanhan.epos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ImportBill extends AppCompatActivity {

    Button them, huy;
    EditText id, code, tenhang, soluong, ngaynhap;
    String scode;
    Integer i_id, maxId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_bill);

        matching();

        Intent intent = getIntent();
        i_id = intent.getIntExtra("ID", -1);
        id.setText(String.valueOf(i_id));

        getProductDetail();

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themPhieuNhap();
            }
        });
    }

    private void themPhieuNhap() {
        try{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("PhieuNhap");
            DatabaseReference hanghoa = database.getReference("HangHoa");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot item : snapshot.getChildren()) {
                        int temp = Integer.parseInt(item.getKey());
                        if (temp > maxId)
                            maxId = temp;
                    }
                    id.setText(String.valueOf(maxId + 1));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            String sid = id.getText().toString().trim();
            String scode = code.getText().toString().trim();
            String stenhang = tenhang.getText().toString().trim();
            String ssoluong = soluong.getText().toString().trim();
            String sngaynhap = ngaynhap.getText().toString().trim();
            String sngaysua = ngaynhap.getText().toString().trim();

            reference.child(sid).push();
            reference.child(sid).child("maCode").setValue(scode);
            reference.child(sid).child("tenHang").setValue(stenhang);
            reference.child(sid).child("soLuong").setValue(ssoluong);
            reference.child(sid).child("ngayNhap").setValue(sngaynhap);
            reference.child(sid).child("ngayThayDoi").setValue(sngaysua);

            String codeHang = hanghoa.child(scode).getKey();
            DatabaseReference dbtonkho = hanghoa.child(codeHang).child("tonKho");

            dbtonkho.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        Integer iton = Integer.parseInt((task.getResult().getValue()).toString());
                        Integer ithem = Integer.parseInt(ssoluong);
                        dbtonkho.setValue(String.valueOf(iton+ithem));
                    }
                }
            });

            DatabaseReference dbgia = hanghoa.child(codeHang).child("donGiaNhap");
            dbgia.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        Integer igia = Integer.parseInt((task.getResult().getValue()).toString());
                        Integer ithem = Integer.parseInt(ssoluong);
                        reference.child(String.valueOf(i_id)).child("thanhTien")
                                .setValue(String.valueOf(igia*ithem));
                    }
                }
            });

            Toast.makeText(ImportBill.this,
                    "Thêm phiếu nhập thành công!", Toast.LENGTH_LONG).show();
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getProductDetail() {
        Intent intent = getIntent();
        scode = intent.getStringExtra("code");

        String sngay= new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("HangHoa");
        reference.child(String.valueOf(scode)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    code.setText(scode);
                    tenhang.setText(hashMap.get("tenHangHoa").toString());
                    ngaynhap.setText(sngay);
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
        them = (Button) findViewById(R.id.btn_nhap_them);
        huy = (Button) findViewById(R.id.btn_nhap_huy);
        id = (EditText) findViewById(R.id.edt_nhap_id);
        code = (EditText) findViewById(R.id.edt_nhap_code);
        tenhang = (EditText) findViewById(R.id.edt_nhap_tenhang);
        soluong = (EditText) findViewById(R.id.edt_nhap_soluong);
        ngaynhap = (EditText) findViewById(R.id.edt_nhap_ngaynhap);
    }
}