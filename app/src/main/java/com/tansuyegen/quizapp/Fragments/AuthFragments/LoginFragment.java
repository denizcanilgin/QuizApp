package com.tansuyegen.quizapp.Fragments.AuthFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tansuyegen.quizapp.Activities.AuthActivity;
import com.tansuyegen.quizapp.Activities.QuizActivity;
import com.tansuyegen.quizapp.R;

public class LoginFragment extends Fragment {


    TextView tv_forgotPass;
    LinearLayout ly_resetPass, ly_main;
    Button bt_login;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,null);

        bt_login = view.findViewById(R.id.bt_signIn);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), QuizActivity.class);
                startActivity(i);
            }
        });
        ly_resetPass = view.findViewById(R.id.ly_resetPass);
        ly_main = view.findViewById(R.id.ly_main);
        tv_forgotPass = view.findViewById(R.id.tv_forgotPass);

        tv_forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ly_main.setVisibility(View.GONE);
                ly_resetPass.setVisibility(View.VISIBLE);


            }
        });



        return view;
    }



}
