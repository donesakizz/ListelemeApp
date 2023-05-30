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

public class GirisActivity extends AppCompatActivity {

    TextView txtKayitOl,txtUnutSifre;
    EditText edtEmail,edtSifre;
    Button btnGirisYap;
    ProgressBar progressBar;
    String strEmail, strSifre;
    String emailPattern ="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        txtUnutSifre = findViewById(R.id.txtUnutSifre);
        txtKayitOl = findViewById(R.id.txtKayitOl);
        edtEmail = findViewById(R.id.edtGirisEmail);
        edtSifre = findViewById(R.id.edtGirisSifre);
        btnGirisYap = findViewById(R.id.btnGirisYap);
        progressBar = findViewById(R.id.signInProgressBar);

        mAuth = FirebaseAuth.getInstance();

        txtKayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GirisActivity.this,KayitActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtUnutSifre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GirisActivity.this,SifreUnutActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        btnGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               strEmail = edtEmail.getText().toString().trim();
               strSifre = edtSifre.getText().toString();

               if(gecerlimi()){
                   GirisYap();
               }

            }
        });

    }

    private void GirisYap() {
        btnGirisYap.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(strEmail, strSifre)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent intent = new Intent(GirisActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GirisActivity.this, "Hata -", Toast.LENGTH_SHORT).show();
                        btnGirisYap.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private boolean gecerlimi(){
        if (TextUtils.isEmpty(strEmail)){
            edtEmail.setError("Email boş olamaz");
            return false;
        }
        if (!strEmail.matches(emailPattern)){
            edtEmail.setError("Geçerli bir email girin");
            return false;
        }
        if (TextUtils.isEmpty(strSifre)){
            edtSifre.setError("Şifre boş olamaz");
            return false;
        }
        return true;
    }
}