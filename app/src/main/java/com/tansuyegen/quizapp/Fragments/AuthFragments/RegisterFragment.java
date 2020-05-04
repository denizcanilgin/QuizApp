package com.tansuyegen.quizapp.Fragments.AuthFragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tansuyegen.quizapp.Activities.AuthActivity;
import com.tansuyegen.quizapp.Activities.QuizActivity;
import com.tansuyegen.quizapp.Activities.QuizesActivity;
import com.tansuyegen.quizapp.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class RegisterFragment extends Fragment {


    EditText et_emailRegister,et_nickname,et_password;
    Button bt_signUp,bt_twitter;
    boolean pass_visibility = false;
    private FirebaseAuth mAuth;
    View view;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    OAuthProvider.Builder provider;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register,null);
        mAuth = FirebaseAuth.getInstance();

        bt_twitter = view.findViewById(R.id.bt_twitter_register);
        bt_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pendingAuthResult();
            }
        });

        et_emailRegister = view.findViewById(R.id.et_emailSignUp);
        bt_signUp = view.findViewById(R.id.bt_signUp);
        et_nickname = view.findViewById(R.id.et_nickname);
        et_password = view.findViewById(R.id.et_RegisterPassword);
        et_password.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                Drawable img_visible = getContext().getResources().getDrawable(R.drawable.ic_visibility_black_24dp);
                Drawable img_invisible = getContext().getResources().getDrawable(R.drawable.ic_visibility_off_black_24dp);
                Drawable img_pass = getContext().getResources().getDrawable(R.drawable.ic_vpn_key_black_24dp);

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (et_password.getRight() - et_password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        if(pass_visibility){
                            //make visible
                            pass_visibility = false;
                            et_password.setCompoundDrawablesWithIntrinsicBounds(img_pass, null, img_invisible, null);
                            et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                        }else{
                            //make invisible
                            pass_visibility = true;
                            et_password.setCompoundDrawablesWithIntrinsicBounds(img_pass, null, img_visible, null);
                            et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }


                        return true;
                    }
                }
                return false;
            }
        });


        provider = OAuthProvider.newBuilder("twitter.com");
        provider.addCustomParameter("lang", "tr");

        bt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTheInputs(et_emailRegister.getText().toString(),et_password.getText().toString(),et_nickname.getText().toString());
            }
        });

        return view;
    }

    private void signUp(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            uploadUserFields(user.getUid() +"");



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            makeToast("Bir hata meydana geldi! Hata mesajı: \n " +  task.getException());

                        }

                        finishLoading(view);


                    }
                });

    }

    private void pendingAuthResult(){

        Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can also be retrieved:
                                    // authResult.getCredential().getAccessToken().
                                    // The OAuth secret can be retrieved by calling:
                                    // authResult.getCredential().getSecret().
                                    Log.i("Twitter_State","" + authResult );
                                    String username = authResult.getAdditionalUserInfo().getUsername();
                                    String user = authResult.getAdditionalUserInfo().getProfile().toString() + "";
                                    Log.i("Twitter_State","" + user);

                                    linkMultipleProviders();
                                    Intent i = new Intent(getActivity(), QuizesActivity.class);
                                    startActivity(i);


                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                    Log.i("Twitter_State","" + e.getMessage() );

                                }
                            });
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.

            Log.i("Twitter_State","NoPendingResult" );

            startActivityForSignIn();


        }

    }

    private void startActivityForSignIn(){

        mAuth
                .startActivityForSignInWithProvider(/* activity= */ getActivity(), provider.build())
                .addOnSuccessListener(
                        new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // User is signed in.
                                // IdP data available in
                                // authResult.getAdditionalUserInfo().getProfile().
                                // The OAuth access token can also be retrieved:
                                // authResult.getCredential().getAccessToken().
                                // The OAuth secret can be retrieved by calling:
                                // authResult.getCredential().getSecret().

                                Log.i("Twitter_State","" + authResult);
                                String user = authResult.getAdditionalUserInfo().getProfile().toString() + "";
                                Log.i("Twitter_State","" + user);

                                String nickname = authResult.getAdditionalUserInfo().getUsername();
                                String email = authResult.getUser().getEmail();
                                String phone = authResult.getUser().getPhoneNumber();
                                String photo_url = authResult.getUser().getPhotoUrl() + "";
                                String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                uploadUserFieldsForTwitterAuth(uId,nickname,email,phone,photo_url);

                                        linkMultipleProviders();
                                Intent i = new Intent(getActivity(), QuizesActivity.class);
                                startActivity(i);

                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure.
                                Log.i("Twitter_State_Error","" + e.getMessage());

                            }
                        });


    }

    private void linkMultipleProviders(){

        // The user is already signed-in.
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        firebaseUser
                .startActivityForLinkWithProvider(/* activity= */ getActivity(), provider.build())
                .addOnSuccessListener(
                        new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // Twitter credential is linked to the current user.
                                // IdP data available in
                                // authResult.getAdditionalUserInfo().getProfile().
                                // The OAuth access token can also be retrieved:
                                // authResult.getCredential().getAccessToken().
                                // The OAuth secret can be retrieved by calling:
                                // authResult.getCredential().getSecret().
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure.
                            }
                        });


    }

    private void checkTheInputs(String str_email, String str_password, String str_nickname){

        if(!str_email.contains("@") || !str_email.contains(".")){

            makeToast("Lütfen geçerli bir email adresi giriniz!");
            return;

        }

        if(str_password.length() < 6){

            makeToast("Şifreniz en az 6 karakter içermelidir!");
            return;

        }

        if(str_nickname.length() < 3){

            makeToast("Lütfen geçerli bir isim/takma isim giriniz!");
            return;

        }

        startLoading(view);
        signUp(str_email,str_password);

    }

    private void makeToast(String str){

        Toast.makeText(getContext(),str,0).show();

    }

    private void startLoading(View view){

        LinearLayout ly_laoding = view.findViewById(R.id.ly_laoding);
        ImageView iv_laoding = view.findViewById(R.id.iv_loading);

        ly_laoding.setVisibility(View.VISIBLE);
        Animation animShake = AnimationUtils.loadAnimation(getContext(), R.anim.anim_shake);
        iv_laoding.startAnimation(animShake);

    }

    private void uploadUserFields(String uId){

        Map<String, String> user_map = new HashMap<>();
        user_map.put("nickname",et_nickname.getText() + "");
        user_map.put("email",et_emailRegister.getText() + "");
        user_map.put("source", "email");

        DocumentReference userRef= db.collection("Users").document(uId);

        userRef.set(user_map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Intent i = new Intent(getActivity(), QuizesActivity.class);
                        startActivity(i);

                    }
                });
    }

    private void uploadUserFieldsForTwitterAuth(String uId,String nickname, String email, String phone, String image_url){


        Map<String, String> user_map = new HashMap<>();
        user_map.put("nickname",nickname);
        user_map.put("email",email);
        user_map.put("phone",phone);
        user_map.put("pp_url",image_url);
        user_map.put("source", "twitter");

        DocumentReference userRef= db.collection("Users").document(uId);

        userRef.set(user_map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Intent i = new Intent(getActivity(), QuizesActivity.class);
                        startActivity(i);

                    }
                });
    }


    private void finishLoading(View view){

        LinearLayout ly_laoding = view.findViewById(R.id.ly_laoding);
        ImageView iv_laoding = view.findViewById(R.id.iv_loading);
        ly_laoding.setVisibility(View.GONE);
//        Animation animShake = AnimationUtils.loadAnimation(getContext(), R.anim.anim_shake);
//        iv_laoding.startAnimation(animShake);

    }


}