package com.tansuyegen.quizapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.tansuyegen.quizapp.Models.LeaderBoardUser;
import com.tansuyegen.quizapp.R;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardAdapter extends ArrayAdapter<LeaderBoardUser> {

    Context context;
    ArrayList<LeaderBoardUser> users;
    LayoutInflater inflater;

    public LeaderBoardAdapter(@NonNull Context context, int resource, @NonNull ArrayList<LeaderBoardUser> objects) {
        super(context, resource, objects);

        inflater = LayoutInflater.from(context);
        this.context = context;
        this.users = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_view_leaderboard,null);

        TextView tv_orderno = convertView.findViewById(R.id.tv_orderno);
        TextView tv_fullname = convertView.findViewById(R.id.tv_fullname);
        TextView tv_point = convertView.findViewById(R.id.tv_point);

        tv_fullname.setText(users.get(position).getUser_nickname());
        tv_point.setText(users.get(position).getUser_point() + " Puan");
        tv_orderno.setText(users.get(position).getUser_order_no() + ".");

        String userId = users.get(position).getUserId() + "";

        if(userId.equals("" + FirebaseAuth.getInstance().getCurrentUser().getUid())){

            tv_fullname.setTextColor(Color.GREEN);

        }

        return  convertView;
    }
}
