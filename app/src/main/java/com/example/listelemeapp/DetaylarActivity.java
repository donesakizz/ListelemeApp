package com.example.listelemeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetaylarActivity extends AppCompatActivity {

    TextView detayNot,detayBaslik,detayTarih;
    ImageView detayResim;
    FloatingActionButton silButton ;
   // FloatingActionButton  guncelleButton;
    String key ="";
    String resimUrl ="";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaylar);

        detayNot = findViewById(R.id.detayNot);
        detayResim = findViewById(R.id.detayResim);
        detayBaslik = findViewById(R.id.detayBaslik);
        detayTarih = findViewById(R.id.detayTarih);
        silButton = findViewById(R.id.silButton);
       // guncelleButton =findViewById(R.id.duzenleButton);


        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            detayNot.setText(bundle.getString("Not"));
            detayBaslik.setText(bundle.getString("Başlık"));
            detayTarih.setText(bundle.getString("Tarih"));
            key = bundle.getString("Key");
            resimUrl = bundle.getString("Resim");
            Glide.with(this).load(bundle.getString("Resim")).into(detayResim);
        }

        silButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Android Calismalari");
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReferenceFromUrl(resimUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(DetaylarActivity.this, "Silindi", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });
            }
        });

       /* guncelleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetaylarActivity.this,GuncellemeActivity.class)
                        .putExtra("Başlık" ,detayBaslik.getText().toString())
                        .putExtra("Not" ,detayNot.getText().toString())
                        .putExtra("Tarih" ,detayTarih.getText().toString())
                        .putExtra("Resim" ,resimUrl)
                        .putExtra("Key" ,key);
                startActivity(intent);


            }
        });

        */






    }
}