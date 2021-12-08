package com.thanhan.epos.ExportData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.thanhan.epos.CaptureAct;
import com.thanhan.epos.ImportBill;
import com.thanhan.epos.R;

import java.util.ArrayList;

public class ExportBill_MainActivity extends AppCompatActivity {
    Button btn_phieuXuat,btn_quetXuat;
    ArrayList listCode;
    Boolean ssCode = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_bill_main);
        matching();
        btn_quetXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });
        btn_phieuXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phieuXuatntent = new Intent(ExportBill_MainActivity.this, ListExportActivity.class);
                startActivity(phieuXuatntent);
            }
        });

        findViewById(R.id.btn_goBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void scanCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Đang quét mã");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null){
            if (result.getContents() != null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                builder.setTitle("Code sản phẩm");

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference hanghoa = database.getReference("HangHoa");

                String macode = result.getContents();
                listCode = new ArrayList();

                builder.setPositiveButton("Thêm phiếu Xuất", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hanghoa.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot item : snapshot.getChildren()) {
                                    listCode.add(item.getKey());
                                }
                                for (int i = 0; i < listCode.size(); i++) {
                                    if (macode.equals(listCode.get(i))== true){
                                        ssCode = true;
                                        break;
                                    }
                                }
                                if (ssCode == true){
                                    Intent intent = new Intent(getApplicationContext(), ExportAddActivity.class);
                                    intent.putExtra("code", macode);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Sản phẩm không tồn tại!",Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("Loi_detail", error.toString());
                            }
                        });
                    }
                }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                Toast.makeText(this,"Không có kết quả",Toast.LENGTH_LONG).show();
            }
        }else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }


    private void matching() {
        btn_phieuXuat = (Button) findViewById(R.id.btn_phieuXuatHang);
        btn_quetXuat = (Button) findViewById(R.id.btn_quetXuat);
    }
}