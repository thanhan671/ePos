package com.thanhan.epos.ExportData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thanhan.epos.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
//import org.threeten.bp.LocalDateTime;

public class ExportAddActivity extends AppCompatActivity {

    Button addItem, cancelItem;
    EditText id, code, tenhang, soluong;
    String scode;
    Integer maxId = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    String currentDate = sdf.format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_add);

        matching();

        getProductDetail();

        cancelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themPhieuXuat();
            }
        });
    }

    private void themPhieuXuat() {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("PhieuXuat");
            DatabaseReference hanghoa = database.getReference("HangHoa");

            String sid = id.getText().toString().trim();
            String scode = code.getText().toString().trim();
            String stenhang = tenhang.getText().toString().trim();
            String ssoluong = soluong.getText().toString().trim();
            String sngayxuat = currentDate;
            String sngaysua = currentDate;

            reference.child(sid).push();

            reference.child(sid).child("maCode").setValue(scode);
            reference.child(sid).child("id").setValue(sid);
            reference.child(sid).child("tenHang").setValue(stenhang);
            reference.child(sid).child("soLuong").setValue(ssoluong);
            reference.child(sid).child("ngayXuat").setValue(sngayxuat);
            reference.child(sid).child("ngayThayDoi").setValue(sngaysua);

            String codeHang = hanghoa.child(scode).getKey();
            DatabaseReference dbtonkho = hanghoa.child(codeHang).child("tonKho");

            if (ssoluong.isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        "Vui lòng nhập số lương hàng!", Toast.LENGTH_LONG).show();
            } else {
                dbtonkho.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            Integer iton = Integer.parseInt((task.getResult().getValue()).toString());
                            Integer ixuat = Integer.parseInt(ssoluong);
                            dbtonkho.setValue(String.valueOf(iton - ixuat));
                        }
                    }
                });

                DatabaseReference dbgia = hanghoa.child(codeHang).child("donGiaXuat");
                dbgia.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            Integer igia = Integer.parseInt((task.getResult().getValue()).toString());
                            Integer ixuat = Integer.parseInt(ssoluong);
                            reference.child(sid).child("thanhTien")
                                    .setValue(String.valueOf(igia * ixuat));
                        }
                    }
                });

                Toast.makeText(ExportAddActivity.this,
                        "Thêm phiếu xuất thành công!", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getProductDetail() {
        Intent intent = getIntent();
        scode = intent.getStringExtra("code");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference phieuxuat = database.getReference("PhieuXuat");
        DatabaseReference hanghoa = database.getReference("HangHoa");

        phieuxuat.addValueEventListener(new ValueEventListener() {
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
                Log.d("Loi_detail", error.toString());
            }
        });

        hanghoa.child(String.valueOf(scode)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    code.setText(scode);
                    tenhang.setText(hashMap.get("tenHangHoa").toString());
                } catch (Exception e) {
                    Log.d("Loi_json", e.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Loi_detail", databaseError.toString());
            }
        });
    }

    private void matching() {
        addItem = (Button) findViewById(R.id.btn_addExport);
        cancelItem = (Button) findViewById(R.id.btn_Cancel);
        id = (EditText) findViewById(R.id.edt_addId);
        code = (EditText) findViewById(R.id.edt_addCode);
        tenhang = (EditText) findViewById(R.id.edt_addTenHang);
        soluong = (EditText) findViewById(R.id.edt_addSoLuong);

    }
}