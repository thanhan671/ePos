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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UpdateImport extends AppCompatActivity{

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
    public void deleteImportBill(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("PhieuNhap");
        DatabaseReference hanghoa = database.getReference("HangHoa");
        String codePhieu = code.getText().toString().trim();
        String codeHang = hanghoa.child(codePhieu).getKey();
        DatabaseReference dbtonkho = database.getReference().child("HangHoa").child(codeHang).child("TonKho");
        if (codePhieu.equals(codeHang)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateImport.this);
            builder.setTitle("Bạn có muốn xóa phiếu nhập này?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbtonkho.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Toast.makeText(UpdateImport.this, snapshot.getValue().toString() ,Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

//                    reference.child(String.valueOf(i_id)).removeValue();
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
            Toast.makeText(UpdateImport.this, "Đã có lỗi!", Toast.LENGTH_LONG).show();
        }
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
        capnhat = (Button) findViewById(R.id.btn_nhap_capnhat);
        xoa = (Button) findViewById(R.id.btn_nhap_xoa);
        huy = (Button) findViewById(R.id.btn_nhap_huy);
        sophieu = (TextView) findViewById(R.id.tv_phieunhap);
        code = (EditText) findViewById(R.id.edt_nhap_code);
        tenhang = (EditText) findViewById(R.id.edt_nhap_tenhang);
        soluong = (EditText) findViewById(R.id.edt_nhap_soluong);
        ngaynhap = (EditText) findViewById(R.id.edt_nhap_ngaynhap);
        ngaysua = (EditText) findViewById(R.id.edt_nhap_ngaysua);
        thanhtien = (EditText) findViewById(R.id.edt_nhap_thanhtien);
    }
}