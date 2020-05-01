package com.tansuyegen.quizapp.Fragments.AuthFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.tansuyegen.quizapp.Activities.AuthActivity;
import com.tansuyegen.quizapp.R;

public class ResetPasswordFragment extends Fragment {
    public ResetPasswordFragment() {

    }
    FirebaseAuth auth;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reset_pass,null);
            auth=FirebaseAuth.getInstance();

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
                  auth.sendPasswordResetEmail(uEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
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

        return view;
    }
}
