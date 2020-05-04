package com.tansuyegen.quizapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tansuyegen.quizapp.Fragments.AuthFragments.LoginFragment;
import com.tansuyegen.quizapp.Fragments.AuthFragments.RegisterFragment;
import com.tansuyegen.quizapp.R;

public class AuthActivity extends AppCompatActivity {

    TextView nav_tv;

    //0 == login ; 1 == register ; 2 == reset_pass
    int current_fragment = 1;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        onCreateMethod();


    }

    private void onCreateMethod(){

        // Initialize Firebase Auth


        nav_tv = findViewById(R.id.tv_nav);
        nav_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                current_fragment++;
                if(current_fragment %2 == 0){
                    loadFragment(new LoginFragment(),false);
                }else{
                    loadFragment(new RegisterFragment(),true);

                }

            }
        });


        loadFragment(new RegisterFragment(),true);

        //To Change The Status Bar Color and Bottom NavBar Color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        window.setNavigationBarColor(Color.parseColor("#5c4db1"));

    }

    //Animation type defines left or right sliding
    private void loadFragment(Fragment fragment, Boolean animation_type) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(animation_type) {
            nav_tv.setText("Zaten bir hesabın var mı? Giriş yap!");
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);}
        else{
            nav_tv.setText("Şimdi kayıt ol!");
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_left);}

        transaction.replace(R.id.fragment_container_auth, fragment);
        transaction.addToBackStack("auth_fragment_backstack");
        transaction.commit();




    }

    @Override
    public void onBackPressed() {

    }
}