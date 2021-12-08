package com.thanhan.epos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thanhan.epos.ExportData.ListExportActivity;

import java.text.NumberFormat;
import java.util.Map;

public class Statistic extends AppCompatActivity {
    private TextView mTotalNhap;
    private TextView mSoPhieuNhap;
    private TextView mSoPhieuXuat;
    private TextView mTotalXuat;
    //firebase
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        matching();
        NumberFormat currentLocale = NumberFormat.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("PhieuNhap");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sum = 0;
                for (DataSnapshot DS : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) DS.getValue();
                    Object thanhtien = map.get("thanhTien");
                    int TValue = Integer.parseInt(String.valueOf(thanhtien));
                    sum += TValue;

                    mTotalNhap.setText(currentLocale.format(sum)+"đ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("PhieuXuat");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sum = 0;
                for (DataSnapshot DS : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) DS.getValue();
                    Object thanhtien = map.get("thanhTien");
                    int TValue = Integer.parseInt(String.valueOf(thanhtien));
                    sum += TValue;

                    mTotalXuat.setText(currentLocale.format(sum)+"đ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("PhieuNhap");
        Query query = rootRef.orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot DtS : snapshot.getChildren()) {
                    mSoPhieuNhap.setText(String.valueOf(DtS.getKey()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference rootRef1 = FirebaseDatabase.getInstance().getReference("PhieuXuat");
        Query query1 = rootRef1.orderByKey().limitToLast(1);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot DtX : snapshot.getChildren()) {
                    mSoPhieuXuat.setText(String.valueOf(DtX.getKey()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        findViewById(R.id.btn_phieunhap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Statistic.this, ListImport.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_phieuxuat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Statistic.this, ListExportActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_thoat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void matching() {
        mSoPhieuNhap = (TextView) findViewById(R.id.tv_PhieuNhap);
        mTotalNhap = (TextView) findViewById(R.id.tv_TongNhap);
        mSoPhieuXuat = (TextView) findViewById(R.id.tv_PhieuXuat);
        mTotalXuat = (TextView) findViewById(R.id.tv_TongXuat);
    }
}