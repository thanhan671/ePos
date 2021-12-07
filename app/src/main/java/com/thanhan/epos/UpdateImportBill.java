package com.thanhan.epos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

public class UpdateImportBill extends AppCompatActivity{

    Button capnhat, xoa, huy;
    EditText code, tenhang, soluong, ngaynhap, ngaysua, thanhtien;
    String i_id;
    TextView sophieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_import_bill);

        matching();

        getImportBillDetail();

        capnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImportBill();
            }
        });

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImportBill();
            }
        });
    }

    private void updateImportBill() {
        try {
            String ssoluong = soluong.getText().toString().trim();
            String sngaysua = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("PhieuNhap");
            DatabaseReference hanghoa = database.getReference("HangHoa");
            String codePhieu = code.getText().toString().trim();
            String codeHang = hanghoa.child(codePhieu).getKey();
            DatabaseReference dbtonkho = hanghoa.child(codeHang).child("tonKho");
            DatabaseReference dbgia = hanghoa.child(codeHang).child("donGiaNhap");

            if (ssoluong.isEmpty()==false){
                dbtonkho.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            Integer iton = Integer.parseInt((task.getResult().getValue()).toString());
                            Integer icapnhat = Integer.parseInt(soluong.getText().toString().trim());
                            reference.child(String.valueOf(i_id)).child("soLuong").get()
                                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            Integer idau = Integer.parseInt((task.getResult().getValue()).toString());
                                            Integer idoi = idau-icapnhat;
                                            dbtonkho.setValue(String.valueOf(iton-idoi));
                                        }
                                    }
                            );
                        }
                    }
                });
                dbgia.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            Integer igia = Integer.parseInt((task.getResult().getValue()).toString());
                            Integer icapnhat = Integer.parseInt(soluong.getText().toString().trim());
                            reference.child(String.valueOf(i_id)).child("thanhTien").setValue(String.valueOf(igia*icapnhat));
                            reference.child(String.valueOf(i_id)).child("soLuong").setValue(ssoluong);
                            reference.child(String.valueOf(i_id)).child("ngayThayDoi").setValue(sngaysua);
                        }
                    }
                });
                Toast.makeText(UpdateImportBill.this,
                        "Cập nhật thành công!", Toast.LENGTH_LONG).show();
                finish();
            }else {
                Toast.makeText(getApplicationContext(),
                        "Vui lòng nhập số lượng hàng!", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Log.d("error update", e.toString());
        }
    }

    public void deleteImportBill(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("PhieuNhap");
        DatabaseReference hanghoa = database.getReference("HangHoa");
        String codePhieu = code.getText().toString().trim();
        String codeHang = hanghoa.child(codePhieu).getKey();
        DatabaseReference dbtonkho = database.getReference().child("HangHoa").child(codeHang).child("tonKho");

        if (codePhieu.equals(codeHang)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateImportBill.this);
            builder.setTitle("Bạn có muốn xóa phiếu nhập này?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbtonkho.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            }
                            else {
                                Integer iton = Integer.parseInt((task.getResult().getValue()).toString());
                                Integer ixoa = Integer.parseInt(soluong.getText().toString().trim());
                                dbtonkho.setValue(String.valueOf(iton-ixoa));
                                reference.child(String.valueOf(i_id)).removeValue();
                                Toast.makeText(getApplicationContext(),"Xóa thành công!",Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    });

                }
            }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }else {
            Toast.makeText(UpdateImportBill.this, "Đã có lỗi!", Toast.LENGTH_LONG).show();
        }
    }

    private void getImportBillDetail() {
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
        capnhat = (Button) findViewById(R.id.btn_sua_capnhat);
        xoa = (Button) findViewById(R.id.btn_sua_xoa);
        huy = (Button) findViewById(R.id.btn_sua_huy);
        sophieu = (TextView) findViewById(R.id.tv_phieunhap);
        code = (EditText) findViewById(R.id.edt_sua_code);
        tenhang = (EditText) findViewById(R.id.edt_sua_tenhang);
        soluong = (EditText) findViewById(R.id.edt_sua_soluong);
        ngaynhap = (EditText) findViewById(R.id.edt_sua_ngaynhap);
        ngaysua = (EditText) findViewById(R.id.edt_sua_ngaysua);
        thanhtien = (EditText) findViewById(R.id.edt_sua_thanhtien);
    }
}