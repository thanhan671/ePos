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
    private RecyclerView rcvProductList;
    private ProductAdapter mProductAdapter;
    private List<ProductObj> mProductList;
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

        getListProduct();
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
        rcvProductList = (RecyclerView) findViewById(R.id.recV_addProduct);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvProductList.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvProductList.addItemDecoration(dividerItemDecoration);

        mProductList = new ArrayList<>();
        mProductAdapter = new ProductAdapter(mProductList, new ProductAdapter.IClickProductListener() {
            @Override
            public void onClickUpdateProduct(ProductObj pro) {
                openDialogUpdateProduct(pro);
            }

            @Override
            public void onClickDeleteProduct(ProductObj pro) {
                deleteProduct(pro);
            }
        });

        rcvProductList.setAdapter(mProductAdapter);

        spnLoaiHH = (Spinner) findViewById(R.id.spnLoaiHH);

        getLoaiHH(spnLoaiHH);
    }

    private void onClickAddProduct(ProductObj p) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("HangHoa");

        String pathObject = String.valueOf(p.getMaCode());
        myRef.child(p.getMaCode()).push();
        myRef.child(pathObject).setValue(p, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(AddProductActivity.this, "Thêm hàng hóa mới thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getListProduct() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("HangHoa");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ProductObj p = snapshot.getValue(ProductObj.class);
                if (p != null) {
                    mProductList.add(p);
                    mProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ProductObj p = snapshot.getValue(ProductObj.class);
                if (p == null || mProductList == null || mProductList.isEmpty()) {
                    return;
                }

                for (int i = 0; i < mProductList.size(); i++) {
                    if (p.getMaCode() == mProductList.get(i).getMaCode()) {
                        mProductList.set(i, p);
                        break;
                    }
                }

                mProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                ProductObj p = snapshot.getValue(ProductObj.class);
                if (p == null || mProductList == null || mProductList.isEmpty()) {
                    return;
                }

                for (int i = 0; i < mProductList.size(); i++) {
                    if (p.getMaCode() == mProductList.get(i).getMaCode()) {
                        mProductList.remove(mProductList.get(i));
                        break;
                    }
                }

                mProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddProductActivity.this, "Lấy dữ liệu thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDialogUpdateProduct(ProductObj pro) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_update_product);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText etEditTenHH = (EditText) dialog.findViewById(R.id.et_dialogUpdateProduct_tenHH);
        EditText etEditDVTinh = (EditText) dialog.findViewById(R.id.et_dialogUpdateProduct_dvTinh);
        EditText etEditDGNhap = (EditText) dialog.findViewById(R.id.et_dialogUpdateProduct_dgNhap);
        EditText etEditDGXuat = (EditText) dialog.findViewById(R.id.et_dialogUpdateProduct_dgXuat);
        EditText etEditTonKho = (EditText) dialog.findViewById(R.id.et_dialogUpdateProduct_tonKho);
        Spinner spnEditLoaiHH = (Spinner) dialog.findViewById(R.id.spn_dialogUpdateProduct_loaiHH);

        Button btnSave = (Button) dialog.findViewById(R.id.btn_dialogUpdateProduct_save);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_dialogUpdateProduct_cancel);

        etEditTenHH.setText(pro.getTenHangHoa());
        etEditDVTinh.setText(pro.getDonViTinh());
        etEditDGNhap.setText(String.valueOf(pro.getDonGiaNhap()));
        etEditDGXuat.setText(String.valueOf(pro.getDonGiaXuat()));
        etEditTonKho.setText(String.valueOf(pro.getTonKho()));

        getLoaiHH(spnEditLoaiHH);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("HangHoa");

                String newTenHH = etEditTenHH.getText().toString().trim();
                pro.setTenHangHoa(newTenHH);

                String newDVTinh = etEditDVTinh.getText().toString().trim();
                pro.setDonViTinh(newDVTinh);

                String newDGNhap = etEditDGNhap.getText().toString().trim();
                pro.setDonGiaNhap(Integer.parseInt(newDGNhap));

                String newDGXuat = etEditDGXuat.getText().toString().trim();
                pro.setDonGiaXuat(Integer.parseInt(newDGXuat));

                String newTonKho = etEditTonKho.getText().toString().trim();
                pro.setTonKho(Integer.parseInt(newTonKho));

                String newSpinnerLoaiHH = spnEditLoaiHH.getSelectedItem().toString();
                pro.setLoaiHangHoa(newSpinnerLoaiHH);

                myRef.child(String.valueOf(pro.getMaCode())).updateChildren(pro.toMapProduct(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(AddProductActivity.this, "Cập nhật Hàng hóa thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();
    }

    private void deleteProduct(ProductObj pro) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Bạn có chắc chắn muốn xóa bản ghi này không?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("HangHoa");

                        myRef.child(String.valueOf(pro.getMaCode())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(AddProductActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void getLoaiHH(Spinner spn) {
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
                spn.setAdapter(mLoaiHHAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(AddProductActivity.this);
        integrator.setCaptureActivity(Capture.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int request, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddProductActivity.this);
                etMaCode.setText(result.getContents());
                //builder.setMessage(result.getContents());
                builder.setTitle("Scanning Result");
                builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanCode();
                    }
                }).setNegativeButton("Finish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(AddProductActivity.this, "No result", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(resultCode, resultCode, data);
        }
    }

}