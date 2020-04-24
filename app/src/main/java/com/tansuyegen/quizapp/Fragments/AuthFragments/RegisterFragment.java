package com.tansuyegen.quizapp.Fragments.AuthFragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tansuyegen.quizapp.R;

public class RegisterFragment extends Fragment {


    EditText et_emailRegister;
    boolean pass_visibility = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,null);
        et_emailRegister = view.findViewById(R.id.et_emailRegister);
        et_emailRegister.setOnTouchListener(new View.OnTouchListener() {
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
                    if(event.getRawX() >= (et_emailRegister.getRight() - et_emailRegister.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        if(pass_visibility){
                            //make visible
                            pass_visibility = false;
                            et_emailRegister.setCompoundDrawablesWithIntrinsicBounds(img_pass, null, img_invisible, null);
                            et_emailRegister.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                        }else{
                            //make invisible
                            pass_visibility = true;
                            et_emailRegister.setCompoundDrawablesWithIntrinsicBounds(img_pass, null, img_visible, null);
                            et_emailRegister.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }


                        return true;
                    }
                }
                return false;
            }
        });


        return view;
    }
}
