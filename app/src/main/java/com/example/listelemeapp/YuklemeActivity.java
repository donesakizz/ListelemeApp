package com.example.listelemeapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class YuklemeActivity extends AppCompatActivity {
    ImageView yuklemeResim;
    Button kaydetButton;
    EditText yuklemeBaslik, yuklemeNot, yuklemeTarih;
    String resimURL;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yukleme);

        yuklemeResim = findViewById(R.id.yukleResim);
        yuklemeNot = findViewById(R.id.yukleTanim);
        yuklemeBaslik = findViewById(R.id.yukleBaslik);
        yuklemeTarih = findViewById(R.id.yukleTarih);
        kaydetButton = findViewById(R.id.kaydetButton);

        ActivityResultLauncher<Intent> activityResultLauncher =  registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            yuklemeResim.setImageURI(uri);
                        }else {
                            Toast.makeText(YuklemeActivity.this, " Herhangi bir resim seçilmedi", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );

        yuklemeResim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        kaydetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kaydetData();
            }
        });



    }

    public  void kaydetData(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images")
                .child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(YuklemeActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.ilerleme_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                resimURL = urlImage.toString();
                yukleData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }

    public void yukleData(){
        String baslik =yuklemeBaslik.getText().toString();
        String not =yuklemeNot.getText().toString();
        String tarih =yuklemeTarih.getText().toString();

        VeriClass dataClass =new VeriClass(baslik ,not, resimURL, tarih);

        //CurrentData
        //b

        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        FirebaseDatabase.getInstance().getReference("Android Calismalari").child(currentDate)
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(YuklemeActivity.this, "Kaydedildi", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(YuklemeActivity.this, "Kaydedilemedi", Toast.LENGTH_SHORT).show();
                    }
                });

    }




}

