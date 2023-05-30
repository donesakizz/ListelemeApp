package com.example.listelemeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class GirisveyaKayitActivity extends AppCompatActivity {

    Button btnGiris,btnKayit;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girisveya_kayit);

        btnGiris=findViewById(R.id.btnAnaGiris);
        btnKayit=findViewById(R.id.btnAnaKayit);


        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(GirisveyaKayitActivity.this,GirisActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GirisveyaKayitActivity.this,KayitActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}