package com.tansuyegen.quizapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tansuyegen.quizapp.R;

import java.io.ByteArrayOutputStream;

public class ProfilActivity extends AppCompatActivity {
    private static final String TAG ="Profil";
    StorageReference storageReference;
    private EditText nick;
    ProgressDialog dialog;
    private EditText pass;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth;
    int IMAGE_CODE=10001;
    ImageView profil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        profil=findViewById(R.id.profdegis);
        dialog=new ProgressDialog(this);
        nick=findViewById(R.id.et_nickname);
        pass=findViewById(R.id.et_RegisterPassword);
        auth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference("Uploads");
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openimage();
            }
        });
    }

    private void openimage() {
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager())!=null){
                startActivityForResult(intent,IMAGE_CODE);

            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMAGE_CODE){
            switch (requestCode){
                case RESULT_OK:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    profil.setImageBitmap(bitmap);
                    handleUpload(bitmap);

            }
        }
    }
    private void handleUpload(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference reference=storageReference.child("profilFoto")
                .child(uid+".jpeg");
            reference.putBytes(baos.toByteArray())
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            getDownUrl(reference);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: ",e.getCause());
                        }
                    });

    }
    private void getDownUrl(StorageReference reference){
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess: "+uri);
                        setUserProfilUrl(uri);
                    }
                });
    }
    private void setUserProfilUrl(Uri uri){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request= new UserProfileChangeRequest.Builder().setPhotoUri(uri)
                .build();
        user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ProfilActivity.this,"Başarıyla yüklendi",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfilActivity.this,"Resim Yüklenemedi. Tekrar Deneyiniz.",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void kayit(View view) {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String newName=nick.getText().toString();
        String newPass=pass.getText().toString();
        if (TextUtils.isEmpty(newName))
            return;
        if (TextUtils.isEmpty(newPass))
            return;
        user.updatePassword(newPass)

                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(ProfilActivity.this, "Güncellendi", Toast.LENGTH_SHORT).show();
            }
        });
        DocumentReference userRef = db.collection("Users").document(FirebaseAuth.getInstance().getUid());

            userRef
                .update("nickname",newName)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent=new Intent(ProfilActivity.this, AuthActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
}
