package com.thanhan.epos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanProductCode extends AppCompatActivity implements View.OnClickListener {

    Button  quetmaBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_product);

        quetmaBtn = findViewById(R.id.btn_quetma);

        findViewById(R.id.btn_scan_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        quetmaBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        quetma();
    }
    private void quetma(){
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
                String codeHang = hanghoa.child(macode).getKey();

                builder.setPositiveButton("Thêm phiếu nhập", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (macode.equals(codeHang)){
                            Intent intent = new Intent(getApplicationContext(), ImportBill.class);
                            intent.putExtra("code", macode);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(),
                                    "Sản phẩm chưa có sẵn trong kho!",Toast.LENGTH_LONG).show();
                        }
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
}