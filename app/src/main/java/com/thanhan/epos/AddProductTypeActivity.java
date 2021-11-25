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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddProductTypeActivity extends AppCompatActivity {
    private ImageView back;
    private EditText edtId, edtName;
    private Button btnAddPT;
    private RecyclerView rcvPT;
    private PTAdapter mPTAdapter;
    private List<ProductType> mListPT;
    private int maxId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_type);

        initUi();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAddPT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int id = Integer.parseInt(edtId.getText().toString().trim());
                    String name = edtName.getText().toString().trim();

                    ProductType pt = new ProductType(id, name);

                    onClickAddPT(pt);
                } catch (Exception e) {
                    Toast.makeText(AddProductTypeActivity.this, "Vui lòng nhập tên loại Sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getListPT();
    }

    private void onClickAddPT(ProductType pt) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Loaihang");

        String pathObject = String.valueOf(pt.getId());
        myRef.child(pathObject).setValue(pt, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(AddProductTypeActivity.this, "Thêm loại hàng hóa mới thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUi() {
        back = (ImageView) findViewById(R.id.imgV_addPT_back);
        edtId = (EditText) findViewById(R.id.et_addPT_id);
        edtName = (EditText) findViewById(R.id.et_addPT_name);
        btnAddPT = (Button) findViewById(R.id.btn_addPT_add);

        rcvPT = (RecyclerView) findViewById(R.id.recV_addPT);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvPT.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvPT.addItemDecoration(dividerItemDecoration);

        mListPT = new ArrayList<>();
        mPTAdapter = new PTAdapter(mListPT, new PTAdapter.iClickListener() {
            @Override
            public void onClickEditItem(ProductType pt) {
                openDialogUpdateItem(pt);
            }

            @Override
            public void onClickDeleteItem(ProductType pt) {
                deleteData(pt);
            }
        });

        rcvPT.setAdapter(mPTAdapter);
    }

    private void getListPT() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Loaihang");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ProductType pt = snapshot.getValue(ProductType.class);
                if (pt != null) {
                    mListPT.add(pt);
                    mPTAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ProductType pt = snapshot.getValue(ProductType.class);
                if (pt == null || mListPT == null || mListPT.isEmpty()) {
                    return;
                }

                for (int i = 0; i < mListPT.size(); i++) {
                    if (pt.getId() == mListPT.get(i).getId()) {
                        mListPT.set(i, pt);
                        break;
                    }
                }

                mPTAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                ProductType pt = snapshot.getValue(ProductType.class);
                if (pt == null || mListPT == null || mListPT.isEmpty()) {
                    return;
                }

                for (int i = 0; i < mListPT.size(); i++) {
                    if (pt.getId() == mListPT.get(i).getId()) {
                        mListPT.remove(mListPT.get(i));
                        break;
                    }
                }

                mPTAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddProductTypeActivity.this, "Lấy dữ liệu thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        setMaxIdItem();
    }

    private void openDialogUpdateItem(ProductType pt) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_edit_pt);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText edtEditName = (EditText) dialog.findViewById(R.id.et_dialogEdit_name);
        Button btnSave = (Button) dialog.findViewById(R.id.btn_dialogEdit_save);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_dialogEdit_cancel);

        edtEditName.setText(pt.getTypeName());

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
                DatabaseReference myRef = database.getReference("Loaihang");

                String newName = edtEditName.getText().toString().trim();
                pt.setTypeName(newName);

                myRef.child(String.valueOf(pt.getId())).updateChildren(pt.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(AddProductTypeActivity.this, "Cập nhật tên loại Sản phẩm thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();
    }

    private void deleteData(ProductType pt) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Bạn có chắc chắn muốn xóa bản ghi này không?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Loaihang");

                        myRef.child(String.valueOf(pt.getId())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(AddProductTypeActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void setMaxIdItem() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Loaihang");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maxId = (int) snapshot.getChildrenCount();
                edtId.setText(String.valueOf(maxId + 1));
                edtId.setEnabled(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}