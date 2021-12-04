package com.thanhan.epos.ExportData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thanhan.epos.R;
import com.thanhan.epos.UpdateImportBill;

public class ListExportActivity extends AppCompatActivity {
    ListView listphieuXuat;
    Button btn_goBack;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_export);
        matching();



        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listphieuXuat.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference("PhieuXuat");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()
                ) {
                    String key = data.getKey();
                    String value = data.getValue().toString();
                    adapter.add(key+"\n"+value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase", "loadPost:onCancelled", databaseError.toException());
            }
        });
        listphieuXuat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = adapter.getItem(position);
                String key = data.split("\n")[0];
                Intent intent = new Intent(getApplicationContext(), ExportItemActivity.class);
                intent.putExtra("ID",key);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_goBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void matching() {
        listphieuXuat = (ListView) findViewById(R.id.lv_phieuXuat);

    }
}