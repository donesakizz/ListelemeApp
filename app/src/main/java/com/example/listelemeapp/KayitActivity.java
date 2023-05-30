package com.example.listelemeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class KayitActivity extends AppCompatActivity {

    TextView txtGiris;

    EditText edtTamAd, edtEmail, edtTelefon, edtSifre, edtSifreOnay;
    ProgressBar progressBar;
    Button btnKayit;
    String strTamAd, strEmail, strTelefon, strSifre, strSifreOnay;
    String emailPattern ="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth mAuth;
    FirebaseFirestore db;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);

        txtGiris=findViewById(R.id.txtGiris);
        edtTamAd=findViewById(R.id.edtTamAd);
        edtEmail=findViewById(R.id.edtEmail);
        edtTamAd=findViewById(R.id.edtTamAd);
        edtTelefon=findViewById(R.id.edtTelefon);
        edtSifre=findViewById(R.id.edtSifre);
        edtSifreOnay=findViewById(R.id.edtSifreOnay);
        progressBar=findViewById(R.id.signUpProgressBar);
        btnKayit =findViewById(R.id.btnKayit);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        txtGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KayitActivity.this,GirisActivity.class);
                startActivity(intent);
               // finish();
            }
        });

        btnKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strTamAd =edtTamAd.getText().toString();
                strEmail =edtEmail.getText().toString().trim();
                strTelefon=edtTelefon.getText().toString().trim();
                strSifre=edtSifre.getText().toString();
                strSifreOnay=edtSifreOnay.getText().toString();

                if (gecerlimi()){
                    KayitOl();
                }
            }
        });


    }

    private void KayitOl() {
        btnKayit.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(strEmail, strSifre).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Map<String,Object> user =new HashMap<>();
                user.put("TamAd", strTamAd);
                user.put("Email",strEmail);
                user.put("Telefon",strTelefon);


                db.collection("Users")
                        .document(strEmail)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Intent intent = new Intent(KayitActivity.this,MainActivity.class);
                                startActivity(intent);
                                //finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(KayitActivity.this, "Hata-", Toast.LENGTH_SHORT).show();
                                btnKayit.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);

                            }
                        });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(KayitActivity.this, "Hata -"+e.getMessage(), Toast.LENGTH_SHORT).show();
                btnKayit.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    private boolean gecerlimi(){
        if (TextUtils.isEmpty(strTamAd)){
            edtTamAd.setError("Ad boş olamaz");
            return false;
        }
        if (TextUtils.isEmpty(strEmail)){
            edtEmail.setError("Email boş olamaz");
            return false;
        }
        if (!strEmail.matches(emailPattern)){
            edtEmail.setError("Geçerli bir email girin");
            return false;
        }
        if (TextUtils.isEmpty(strTelefon)){
            edtTelefon.setError("Telefon numarası boş olamaz");
            return false;
        }
        if (TextUtils.isEmpty(strSifre)){
            edtSifre.setError("Şifre boş olamaz");
            return false;
        }
        if (TextUtils.isEmpty(strSifreOnay)){
            edtSifreOnay.setError("Şifre Onayla kısmı boş olamaz");
            return false;
        }
        if (!strSifre.equals(strSifreOnay)){
            edtSifreOnay.setError("Sifre onayla ve aynı olmalı");
            return false;
        }
        return true;
    }
}