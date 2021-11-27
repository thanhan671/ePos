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

public class AdminAC extends AppCompatActivity {
    ListView Listac;
    ArrayAdapter<String> adapter;
    ImageView back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        matching();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line);
        Listac.setAdapter(adapter);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("NhanVien");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear();
                for (DataSnapshot data:snapshot.getChildren()
                     ) {
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



    }

    private void matching() {
        Listac = (ListView) findViewById(R.id.lv_nvac);
        back = (ImageView) findViewById(R.id.imgV_AdminAC_back);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menuac,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.mnuAddAC){
            //new activity
            String data = adapter.getItem(adapter.getCount()-1);
            String sid = data.split("\n")[0];
            int i_id = Integer.parseInt(sid) + 1;
            Intent intent = new Intent(this,add_admin_ac.class);
            intent.putExtra("ID",i_id);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.mnuAdminAC){
            Toast.makeText(this,"Wellcom Admin",Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}