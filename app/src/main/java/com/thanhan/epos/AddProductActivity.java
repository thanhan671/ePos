package com.thanhan.epos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {
    private ImageView back;
    private EditText etTenHH, etMaCode, etDVTinh, etDGNhap, etDGXuat, etTonKho;
    private Button btnAddProduct, btnScanCode;
    private Spinner spnLoaiHH;
    private List<String> loaiHH;
    private ArrayAdapter<String> mLoaiHHAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        initUi();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maCode = etMaCode.getText().toString().trim();
                String tenHH = etTenHH.getText().toString().trim();
                String loaiHH = spnLoaiHH.getSelectedItem().toString();
                String dvTinh = etDVTinh.getText().toString().trim();
                String dgNhap = etDGNhap.getText().toString().trim();
                String dgXuat = etDGXuat.getText().toString().trim();
                String tonKho = etTonKho.getText().toString().trim();

                if (maCode.isEmpty()) {
                    Toast.makeText(AddProductActivity.this, "Vui lòng nhập hoặc quét mã Code", Toast.LENGTH_SHORT).show();
                } else if (tenHH.isEmpty()) {
                    Toast.makeText(AddProductActivity.this, "Vui lòng nhập tên Hàng hóa", Toast.LENGTH_SHORT).show();
                } else if (dvTinh.isEmpty()) {
                    Toast.makeText(AddProductActivity.this, "Vui lòng nhập Đơn vị tính", Toast.LENGTH_SHORT).show();
                } else if (tonKho.isEmpty() || Integer.parseInt(tonKho) < 0) {
                    Toast.makeText(AddProductActivity.this, "Số lượng tồn kho phải lớn hơn hoặc bằng 0", Toast.LENGTH_SHORT).show();
                } else if (dgNhap.isEmpty() || Integer.parseInt(dgNhap) <= 0) {
                    Toast.makeText(AddProductActivity.this, "Đơn giá nhập phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                } else if (dgXuat.isEmpty() || Integer.parseInt(dgXuat) <= 0) {
                    Toast.makeText(AddProductActivity.this, "Đơn giá xuất phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                } else {
                    ProductObj pro = new ProductObj(Integer.parseInt(dgNhap), Integer.parseInt(dgXuat), dvTinh, loaiHH, maCode, tenHH, Integer.parseInt(tonKho));
                    onClickAddProduct(pro);
                }
            }
        });
    }

    private void initUi() {
        back = (ImageView) findViewById(R.id.imgV_addProduct_back);
        etTenHH = (EditText) findViewById(R.id.et_addProduct_tenHH);
        etMaCode = (EditText) findViewById(R.id.et_addProduct_maCode);
        etDVTinh = (EditText) findViewById(R.id.et_addProduct_dvTinh);
        etDGNhap = (EditText) findViewById(R.id.et_addProduct_dgNhap);
        etDGXuat = (EditText) findViewById(R.id.et_addProduct_dgXuat);
        etTonKho = (EditText) findViewById(R.id.et_addProduct_tonKho);
        btnAddProduct = (Button) findViewById(R.id.btn_addProduct_addPro);
        btnScanCode = (Button) findViewById(R.id.btn_addProduct_scanCode);

        spnLoaiHH = (Spinner) findViewById(R.id.spnLoaiHH);
        getLoaiHH();
    }

    private void onClickAddProduct(ProductObj p) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("HangHoa");

        myRef.child(p.getMaCode()).push();
        myRef.child(p.getMaCode()).setValue(p, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(AddProductActivity.this, "Thêm hàng hóa mới thành công", Toast.LENGTH_SHORT).show();

                etMaCode.setText("");
                etTenHH.setText("");
                spnLoaiHH.setSelection(0);
                etDVTinh.setText("");
                etTonKho.setText("");
                etDGNhap.setText("");
                etDGXuat.setText("");
                etMaCode.requestFocus();
            }
        });
    }

    private void getLoaiHH() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("LoaiHang");

        loaiHH = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String spinnerLoaiHH = dataSnapshot.child("typeName").getValue(String.class);
                    loaiHH.add(spinnerLoaiHH);
                }

                mLoaiHHAdapter = new ArrayAdapter<>(AddProductActivity.this, R.layout.item_spinner, loaiHH);
                mLoaiHHAdapter.setDropDownViewResource(R.layout.item_spinner);
                spnLoaiHH.setAdapter(mLoaiHHAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(Capture.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Đang quét mã");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int request, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(request,resultCode,data);
        if (result != null){
            if (result.getContents() != null){
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                builder.setTitle("Code sản phẩm");

                builder.setPositiveButton("Quét lại", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanCode();
                    }
                }).setNegativeButton("Hoàn thành", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        etMaCode.setText(result.getContents());
                    }
                });

                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                Toast.makeText(this,"Không có kết quả",Toast.LENGTH_LONG).show();
            }
        }else {
            super.onActivityResult(request,resultCode,data);
        }
    }

}