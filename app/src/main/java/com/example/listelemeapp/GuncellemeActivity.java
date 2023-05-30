package com.example.listelemeapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class GuncellemeActivity extends AppCompatActivity {

    ImageView guncelleResim;
    //Button guncelleButton;
    EditText guncelleNot, guncelleBaslik, guncelleTarih;
    String baslik,not,tarih;
    String resimUrl;
    String key, eskiResimUrl;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guncelleme);

        //guncelleButton= findViewById(R.id.duzenleButton);
        guncelleNot =findViewById(R.id.guncelleNot);
        guncelleResim =findViewById(R.id.guncelleResim);
        guncelleTarih =findViewById(R.id.guncelleTarih);
        guncelleBaslik =findViewById(R.id.guncelleBaslik);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data =result.getData();//veri data yapıldı
                            uri = data.getData();//''
                            guncelleResim.setImageURI(uri);
                        }else {
                            Toast.makeText(GuncellemeActivity.this, "Herhangi bir resim seçilmedi", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

        );
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            Glide.with(GuncellemeActivity.this).load(bundle.getString("Resim")).into(guncelleResim);
            guncelleBaslik.setText(bundle.getString("Başlık"));
            guncelleNot.setText(bundle.getString("Not"));
            guncelleTarih.setText(bundle.getString("Tarih"));
            key = bundle.getString("Key");
            eskiResimUrl = bundle.getString("Resim");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Android Calismalari").child(key);
        guncelleResim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        /*guncelleButton.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                //pending
                kaydetVeri();
                Intent intent= new Intent(GuncellemeActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

         */


    }

    public void kaydetVeri(){
        storageReference = FirebaseStorage.getInstance().getReference().child("Android Images").child(uri.getLastPathSegment());

        AlertDialog.Builder builder= new AlertDialog.Builder(GuncellemeActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.ilerleme_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlResim= uriTask.getResult();
                resimUrl = urlResim.toString();
                guncelleVeri();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }


    public void guncelleVeri(){
        baslik = guncelleBaslik.getText().toString().trim();
        not  = guncelleNot.getText().toString().trim();
        baslik = guncelleBaslik.getText().toString().trim();
        tarih = guncelleTarih.getText().toString().trim();//lang trim yazmadı

        VeriClass veriClass = new VeriClass(baslik, not, tarih,resimUrl);

        databaseReference.setValue(veriClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    StorageReference reference = FirebaseStorage.getInstance().getReference(eskiResimUrl);
                    reference.delete();
                    Toast.makeText(GuncellemeActivity.this, "Not Düzenlendi", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GuncellemeActivity.this,"Not düzenlenirken hata oldu", Toast.LENGTH_SHORT).show();
            }
        });

    }

}