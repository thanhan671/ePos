package com.thanhan.epos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StaffAcountActivity extends AppCompatActivity {
    private ListView listStaffAcc;
    private ArrayAdapter<String> adapter;
    private ImageView back;
    private Button them;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_acount);

        matching();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapter = new ArrayAdapter<>(StaffAcountActivity.this, android.R.layout.simple_dropdown_item_1line);
        listStaffAcc.setAdapter(adapter);

        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = adapter.getItem(adapter.getCount()-1);
                String sid = data.split("\n")[0];
                int i_id = Integer.parseInt(sid) + 1;
                Intent intent = new Intent(StaffAcountActivity.this, AddAcountStaffActivity.class);
                intent.putExtra("ID",i_id);
                startActivity(intent);
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("NhanVien");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String key = data.getKey();
                    String value = data.getValue().toString();
                    adapter.add(key + "\n" + value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase","LoadPost:onCancelled",error.toException());
            }
        });

        listStaffAcc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = adapter.getItem(position);
                String key = data.split("\n")[0];
                Intent intent = new Intent(StaffAcountActivity.this, EditStaffAcountActivity.class);
                intent.putExtra("ID", key);
                startActivity(intent);
            }
        });
    }

    private void matching() {
        listStaffAcc = (ListView) findViewById(R.id.lv_staffAccList);
        back = (ImageView) findViewById(R.id.imgV_staffAcc_back);
        them = (Button) findViewById(R.id.btn_acc_add);
    }
}