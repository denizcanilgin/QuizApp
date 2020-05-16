package com.tansuyegen.quizapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tansuyegen.quizapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilActivity extends AppCompatActivity {
    private static final String TAG ="Profil";
    StorageReference storageReference;
    private EditText et_nickname;
    ProgressDialog dialog;
    private EditText pass;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth;
    int IMAGE_CODE=10001;
    ImageView iv_pp_image,iv_menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        iv_pp_image = (ImageView) findViewById(R.id.profdegis);
        dialog=new ProgressDialog(this);
        et_nickname=findViewById(R.id.et_nickname);
        pass=findViewById(R.id.et_RegisterPassword);
        auth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference("Uploads");
        iv_menuIcon = findViewById(R.id.iv_menuIcon_profile);
        iv_menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showMenuDialog();
            }
        });

        iv_pp_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType(ProfilActivity.this);
            }
        });

        queryUserProfile(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        iv_pp_image.setImageBitmap(selectedImage);
                        handleUpload(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImg = data.getData();
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(bitmap != null) {
                            iv_pp_image.setImageBitmap(bitmap);
                            handleUpload(bitmap);
                        }


                    }
                    break;

                default:                            Log.i("PicturePath", "CODE" );


                    break;

            }
        }else{Log.i("PicturePath","CANCELLED");}
    }
    private void handleUpload(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,15,baos);
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference reference=storageReference.child("ProfilePhotos")
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
                        setUserProfileUrl(uri);
                    }
                });
    }
    private void setUserProfileUrl(Uri uri){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request= new UserProfileChangeRequest.Builder().setPhotoUri(uri)
                .build();
        user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });


        DocumentReference userRef = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef
                .update("ImageURL", uri + "")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        Toast.makeText(ProfilActivity.this,"Başarıyla yüklendi",Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                        Toast.makeText(ProfilActivity.this,"Resim Yüklenemedi. Tekrar Deneyiniz.",Toast.LENGTH_LONG).show();

                    }
                });


    }
    public void saveProfileAction(View view) {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String newName=et_nickname.getText().toString();
        String newPass=pass.getText().toString();

        if(newPass.length() > 7) {
            user.updatePassword(newPass)

                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                Toast.makeText(ProfilActivity.this, "Şifreniz Güncellendi", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{

            //Do nothing

        }

        if(newName.length() > 2){
        DocumentReference userRef = db.collection("Users").document(FirebaseAuth.getInstance().getUid());
            userRef
                .update("nickname",newName)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProfilActivity.this, "Profiliniz Güncellendi", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });}

        else{

            //do nothing

        }
    }
    private void selectType(Context context) {
        final Dialog d_picker = new Dialog(context);
        d_picker.setCancelable(true);
        d_picker.setContentView(R.layout.dialog_media_picker);
        d_picker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tv_gallery = d_picker.findViewById(R.id.tv_pickFromGallery);
        TextView tv_camera = d_picker.findViewById(R.id.tv_takeAPhoto);

        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
                d_picker.dismiss();
            }
        });

        tv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
                d_picker.dismiss();
            }
        });

        d_picker.show();

    }
    private void queryUserProfile(String uID){
        startLoading();
        DocumentReference docRef = db.collection("Users").document(uID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        fillUserProfile(document);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }
    private void fillUserProfile(DocumentSnapshot document) {

        String img_url = document.getData().get("ImageURL") + "";
        String nickname = document.getData().get("nickname") + "";

        et_nickname.setText(nickname);

        Picasso.get()
                .load(img_url)
                .placeholder(R.drawable.personalloginnnnn)
                .into(iv_pp_image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        //do smth when picture is loaded successfully
                        finishLoading();
                    }

                    @Override
                    public void onError(Exception ex) {
                        //do smth when there is picture loading error
                        finishLoading();
                    }
                });



    }
    private void startLoading(){

        LinearLayout ly_laoding = findViewById(R.id.ly_laoding);
        ImageView iv_laoding = findViewById(R.id.iv_loading);

        iv_laoding.bringToFront();
        ly_laoding.bringToFront();

        ly_laoding.setVisibility(View.VISIBLE);
        Animation animShake = AnimationUtils.loadAnimation(this, R.anim.anim_shake);
        iv_laoding.startAnimation(animShake);

    }
    private void finishLoading(){

        LinearLayout ly_laoding = findViewById(R.id.ly_laoding);
        ImageView iv_laoding = findViewById(R.id.iv_loading);
        ly_laoding.setVisibility(View.GONE);
//        Animation animShake = AnimationUtils.loadAnimation(getContext(), R.anim.anim_shake);
//        iv_laoding.startAnimation(animShake);

    }
    private void showMenuDialog(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_menu);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    public void profil(View view) {
        Intent intent=new Intent(ProfilActivity.this, ProfilActivity.class);
        startActivity(intent);
    }

    public void Cikis1(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(ProfilActivity.this,AuthActivity.class);
        startActivity(intent);
        finish();
    }

    public void Anasayfa(View view) {
        Intent intent=new Intent(ProfilActivity.this,QuizesActivity.class);
        startActivity(intent);
        finish();
    }

}
