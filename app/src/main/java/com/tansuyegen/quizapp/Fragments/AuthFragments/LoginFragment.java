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
import com.tansuyegen.quizapp.Activities.AuthActivity;
import com.tansuyegen.quizapp.Activities.QuizActivity;
import com.tansuyegen.quizapp.Activities.QuizesActivity;
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

public class LoginFragment extends Fragment {

    TwitterLoginButton loginButton;
    FirebaseAuth firebaseAuth;
    TextView tv_forgotPass;
    EditText et_email,et_pass;
    LinearLayout ly_resetPass, ly_main,resss;
    Button bt_login,twittergiris;
    View view;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login,null);
        resetPass();
       // Twitter.initialize(getActivity());
        TwitterConfig config = new TwitterConfig.Builder(getActivity())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("y0JfGrWdFBPCoWOVgMSzL9KQp", "nJst3CoJ5pL2UvN8iCzzHIikFC7IZL9DWUGiuu4zOA7jHKtkVC"))
                .debug(true)
                .build();
        Twitter.initialize(config);
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
        loginButton = (TwitterLoginButton)view.findViewById(R.id.bt_twitter);
        tv_forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ly_main.setVisibility(View.GONE);
                ly_resetPass.setVisibility(View.VISIBLE);



            }
        });

        OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");
        provider.addCustomParameter("lang", "tr");

        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                login(session);
            }

            @Override
            public void failure(TwitterException exception) {
                // ... do something
            }
        });
//
        return view;
    }
    public void login(TwitterSession session){
        String username=session.getUserName();
        FirebaseUser user = mAuth.getCurrentUser();

        Intent i = new Intent(getActivity(), QuizesActivity.class);
        startActivity(i);
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

        loginButton.onActivityResult(requestCode, resultCode, data);


    }
}


