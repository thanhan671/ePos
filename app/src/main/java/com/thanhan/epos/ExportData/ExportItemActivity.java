package com.thanhan.epos.ExportData;

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
import com.thanhan.epos.R;
import com.thanhan.epos.UpdateImportBill;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ExportItemActivity extends AppCompatActivity {

    Button updateItem, delItem, cancelItem,printItem;
    EditText macode, tenHang, soLuong, ngayXuat, ngaySua, thanhTien, idDon;
    String i_id;
    TextView sophieuXuat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_item);
        matching();
//        get list export Item
        getExportItemDetail();
//        Go back List Export Item
        cancelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        add Export Item

//        delete Export Item
        delItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteExportItem();
            }
        });
//        update Export Item
        updateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateExportItem();
            }
        });
//        print Export Item


    }

    private void updateExportItem() {
        try {
            String soluongSua = soLuong.getText().toString().trim();
            String ngaySuaItem = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("PhieuXuat");
            DatabaseReference hanghoa = database.getReference("HangHoa");
            String codePhieu = macode.getText().toString().trim();
            String codeHang = hanghoa.child(codePhieu).getKey();
            DatabaseReference dbtonkho = hanghoa.child(codeHang).child("tonKho");

            dbtonkho.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        Integer iton = Integer.parseInt((task.getResult().getValue()).toString());
                        Integer icapnhat = Integer.parseInt(soLuong.getText().toString().trim());
                        reference.child(String.valueOf(i_id)).child("soLuong").get().addOnCompleteListener(
                                new OnCompleteListener<DataSnapshot>() {
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

            DatabaseReference dbgia = hanghoa.child(codeHang).child("donGiaNhap");
            dbgia.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        Integer igia = Integer.parseInt((task.getResult().getValue()).toString());
                        Integer icapnhat = Integer.parseInt(soLuong.getText().toString().trim());
                        reference.child(String.valueOf(i_id)).child("thanhTien").setValue(String.valueOf(igia*icapnhat));
                        reference.child(String.valueOf(i_id)).child("soLuong").setValue(soluongSua);
                        reference.child(String.valueOf(i_id)).child("ngayThayDoi").setValue(ngaySuaItem);
                    }
                }
            });

            Toast.makeText(ExportItemActivity.this,
                    "Cập nhật thành công!", Toast.LENGTH_LONG).show();
            finish();
        }catch (Exception e){
            Log.d("error update export", e.toString());
        }
    }

    public void deleteExportItem(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("PhieuNhap");
        DatabaseReference hanghoa = database.getReference("HangHoa");
        String codePhieu = macode.getText().toString().trim();
        String codeHang = hanghoa.child(codePhieu).getKey();
        DatabaseReference dbtonkho = database.getReference().child("HangHoa").child(codeHang).child("tonKho");
        if (codePhieu.equals(codeHang)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ExportItemActivity.this);
            builder.setTitle("Bạn có muốn xóa phiếu Xuất này?");
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
                                Integer ixoa = Integer.parseInt(soLuong.getText().toString().trim());
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
            Toast.makeText(ExportItemActivity.this, "Đã có lỗi!", Toast.LENGTH_LONG).show();
        }
    }

    private void getExportItemDetail() {
        Intent intent = getIntent();
        i_id = intent.getStringExtra("ID");
        sophieuXuat.setText("Phiếu nhập số: "+String.valueOf(i_id));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("PhieuXuat");
        reference.child(String.valueOf(i_id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    macode.setText(hashMap.get("maCode").toString());
                    idDon.setText(hashMap.get("id").toString());
                    tenHang.setText(hashMap.get("tenHang").toString());
                    soLuong.setText(hashMap.get("soLuong").toString());
                    ngayXuat.setText(hashMap.get("ngayXuat").toString());
                    ngaySua.setText(hashMap.get("ngayThayDoi").toString());
                    thanhTien.setText(hashMap.get("thanhTien").toString());
                } catch (Exception e) {
                    Log.d("Error Json File",e.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ExportItemActivity.this, "Lỗi Render phiếu xuất", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void matching() {
        updateItem = (Button) findViewById(R.id.btn_UpdateExportItem);
        delItem = (Button) findViewById(R.id.btn_DelExportItem);
        cancelItem = (Button) findViewById(R.id.btn_Cancel);
        idDon = (EditText) findViewById(R.id.edt_IdDon);
        sophieuXuat = (TextView) findViewById(R.id.tv_phieuXuat);
        macode = (EditText) findViewById(R.id.edt_maCode);
        tenHang = (EditText) findViewById(R.id.edt_TenHang);
        soLuong = (EditText) findViewById(R.id.edt_soLuong);
        ngayXuat = (EditText) findViewById(R.id.edt_NgayNhap);
        ngaySua = (EditText) findViewById(R.id.edt_NgaySua);
        thanhTien = (EditText) findViewById(R.id.edt_ThanhTien);
    }
}