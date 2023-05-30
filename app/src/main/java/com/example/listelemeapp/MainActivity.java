package com.example.listelemeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fbtnNotEkle ,fbtnCikis;
    RecyclerView recyclerView;
    List<VeriClass> veriList;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    SearchView searchView;
    MyListeAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbtnNotEkle = findViewById(R.id.fBtnNotEkle);
        fbtnCikis = findViewById(R.id.fBtnCikis);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.arama);
        searchView.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.ilerleme_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        veriList = new ArrayList<>();

        adapter= new MyListeAdapter(MainActivity.this, veriList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Android Calismalari");
        dialog.show();

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                veriList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()){
                    VeriClass veriClass= itemSnapshot.getValue(VeriClass.class);
                    veriClass.setKey(itemSnapshot.getKey());
                    veriList.add(veriClass);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                aramaList(newText);
                return true;
            }
        });


        fbtnNotEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,YuklemeActivity.class);
                startActivity(intent);
            }
        });

        //Cıkış butonu
        fbtnCikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this ,GirisveyaKayitActivity.class);
                startActivity(intent);
            }
        });
    }




    public void aramaList(String text){
        ArrayList<VeriClass> aramaList = new ArrayList<>();
        for (VeriClass veriClass:veriList){
            if (veriClass.getDataBaslik().toLowerCase().contains(text.toLowerCase())){
                aramaList.add(veriClass);
            }
        }
        adapter.aramaVeriList(aramaList);
    }

}