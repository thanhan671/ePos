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

import java.util.ArrayList;
import java.util.List;

public class ViewListProductActivity extends AppCompatActivity {
    private ImageView back;
    private Button btnAddNewProduct;
    private RecyclerView rcvProductList;
    private ProductAdapter mProductAdapter;
    private List<ProductObj> mProductList;
    private List<String> loaiHH;
    private ArrayAdapter<String> mLoaiHHAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_product);

        initUi();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewListProductActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        getListProduct();
    }

    private void initUi() {
        back = (ImageView) findViewById(R.id.imgV_viewListProduct_back);
        btnAddNewProduct = (Button) findViewById(R.id.btn_listProduct_addNew);
        rcvProductList = (RecyclerView) findViewById(R.id.recV_listProduct);

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
                Toast.makeText(ViewListProductActivity.this, "Lấy dữ liệu thất bại", Toast.LENGTH_SHORT).show();
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

        getLoaiHHEdit(pro.getLoaiHangHoa(), spnEditLoaiHH);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("LoaiHang");

        loaiHH = new ArrayList<>();

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
                        Toast.makeText(ViewListProductActivity.this, "Cập nhật Hàng hóa thành công", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(ViewListProductActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void getLoaiHHEdit(String containKey, Spinner spnLoaiHHEdit) {
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

                mLoaiHHAdapter = new ArrayAdapter<>(ViewListProductActivity.this, R.layout.item_spinner, loaiHH);
                mLoaiHHAdapter.setDropDownViewResource(R.layout.item_spinner);
                spnLoaiHHEdit.setAdapter(mLoaiHHAdapter);

                for (int i = 0; i < spnLoaiHHEdit.getAdapter().getCount(); i++) {
                    if (spnLoaiHHEdit.getAdapter().getItem(i).toString().contains(containKey)) {
                        spnLoaiHHEdit.setSelection(i);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}