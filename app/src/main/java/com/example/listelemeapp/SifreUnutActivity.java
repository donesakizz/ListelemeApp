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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SifreUnutActivity extends AppCompatActivity {

    Button btnSifirla , btnGeri;
    EditText edtEmail;
    String strEmail;
    ProgressBar progressBar;
    String emailPattern ="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_unut);

        btnSifirla = findViewById(R.id.btnSifirla);
        btnGeri = findViewById(R.id.btnUnutSifreGeri);
        edtEmail = findViewById(R.id.edtUnutSifreEmail);
        progressBar = findViewById(R.id.forgetPasswordProgressbar);

        mAuth = FirebaseAuth.getInstance();

        btnGeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSifirla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strEmail = edtEmail.getText().toString().trim();
                if(gecerlimi()) {
                    SifirlaSifre();
                }
            }
        });
    }

    private void SifirlaSifre() {
        btnSifirla.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        mAuth.sendPasswordResetEmail(strEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(SifreUnutActivity.this, "Şifreniz sıfırlandı yeni şifre ile giriş yapabilirsiniz!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SifreUnutActivity.this,GirisActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SifreUnutActivity.this, "Hata -" , Toast.LENGTH_SHORT).show();
                btnSifirla.setVisibility(View.VISIBLE);
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

        return true;
    }
}