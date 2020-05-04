package com.tansuyegen.quizapp.Fragments.AuthFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.tansuyegen.quizapp.Activities.AuthActivity;
import com.tansuyegen.quizapp.Activities.QuizActivity;
import com.tansuyegen.quizapp.Activities.QuizesActivity;
import com.tansuyegen.quizapp.Models.Quiz;
import com.tansuyegen.quizapp.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

    Button loginButton;
    TextView tv_forgotPass;
    EditText et_email,et_pass;
    LinearLayout ly_resetPass, ly_main,resss;
    Button bt_login,bt_twitter;
    View view;
    private FirebaseAuth mAuth;
    OAuthProvider.Builder provider;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login,null);
        resetPass();

        mAuth = FirebaseAuth.getInstance();
        resss=view.findViewById(R.id.respasss);
        et_email = view.findViewById(R.id.et_emailLogin);
        et_pass = view.findViewById(R.id.et_passLogin);

        bt_login = view.findViewById(R.id.bt_signIn);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTheInputs(et_email.getText().toString(),et_pass.getText().toString());
            }
        });
        ly_resetPass = view.findViewById(R.id.ly_resetPass);
        ly_main = view.findViewById(R.id.ly_main);
        tv_forgotPass = view.findViewById(R.id.tv_forgotPass);
        bt_twitter = (Button) view.findViewById(R.id.bt_twitter_login);
        bt_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pendingAuthResult();
            }
        });

        tv_forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ly_main.setVisibility(View.GONE);
                ly_resetPass.setVisibility(View.VISIBLE);



            }
        });

        provider = OAuthProvider.newBuilder("twitter.com");
        provider.addCustomParameter("lang", "tr");

        return view;
    }


    private void signIn(String str_email, String str_password){

        startLoading(view);

        mAuth.signInWithEmailAndPassword(str_email, str_password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent i = new Intent(getActivity(), QuizesActivity.class);
                            startActivity(i);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            makeToast("Email veya şifreniz yanlış!");
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
                                String username = authResult.getAdditionalUserInfo().getUsername();
                                String user = authResult.getAdditionalUserInfo().getProfile().toString() + "";
                                Log.i("Twitter_State","" + user);



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

    private void uploadUserFields(String uId){


//        Map<String, String> user_map = new HashMap<>();
//        user_map.put("nickname",et_nickname.getText() + "");
//        user_map.put("email",et_emailRegister.getText() + "");
//        user_map.put("source", "email");
//
//        DocumentReference userRef= db.collection("Users").document(uId);
//
//        userRef.set(user_map)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
//                        Intent i = new Intent(getActivity(), QuizesActivity.class);
//                        startActivity(i);
//
//                    }
//                });
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

    private void finishLoading(View view){

        LinearLayout ly_laoding = view.findViewById(R.id.ly_laoding);
        ImageView iv_laoding = view.findViewById(R.id.iv_loading);
        ly_laoding.setVisibility(View.GONE);
//        Animation animShake = AnimationUtils.loadAnimation(getContext(), R.anim.anim_shake);
//        iv_laoding.startAnimation(animShake);

    }

    private void checkTheInputs(String str_email, String str_password){

        if(!str_email.contains("@") || !str_email.contains(".")){

            makeToast("Lütfen geçerli bir email adresi giriniz!");
            return;

        }

        if(str_password.length() < 6){

            makeToast("Şifreniz en az 6 karakter içermelidir!");
            return;

        }



        startLoading(view);
        signIn(str_email,str_password);

    }

    private void resetPass() {

        final EditText sifre=(EditText)view.findViewById(R.id.et_emailForPass);
        Button button=(Button)view.findViewById(R.id.bt_sendResetLink);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uEmail=sifre.getText().toString();
                if (TextUtils.isEmpty(uEmail))
                {
                    Toast.makeText(getActivity(),"Lütfen E-mail'nizi giriniz.",Toast.LENGTH_LONG).show();
                }else
                {
                    mAuth.sendPasswordResetEmail(uEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(getActivity(),"Başarılı",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getActivity(), AuthActivity.class));
                            }else {
                                String mesaj=task.getException().getMessage();
                                Toast.makeText(getActivity(),"Hata"+mesaj,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.

//        loginButton.onActivityResult(requestCode, resultCode, data);


    }
}


