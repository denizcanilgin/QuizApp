package com.tansuyegen.quizapp.Adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.tansuyegen.quizapp.Activities.QuizesActivity;
import com.tansuyegen.quizapp.Models.Quiz;
import com.tansuyegen.quizapp.R;

import java.util.ArrayList;
import java.util.List;

public class QuizesAdapter extends ArrayAdapter<Quiz> {

    Context context;
    ArrayList<Quiz> quizes;
    LayoutInflater inflater;

    public QuizesAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Quiz> objects) {
        super(context, resource, objects);

        context = context;
        quizes = objects;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_view_quiz,null);

        ImageView iv_stateImage = convertView.findViewById(R.id.iv_stateImage);
        ImageView iv_quizIcon = convertView.findViewById(R.id.iv_quizIcon);
        TextView tv_quizTitle = convertView.findViewById(R.id.tv_quizTitle);
        TextView tv_numberOfQuestions = convertView.findViewById(R.id.tv_numberOfQuestions);

        tv_quizTitle.setText(quizes.get(position).getTitle());
        tv_numberOfQuestions.setText(quizes.get(position).getNumberOfQuestions() + " Soru");
        Picasso.get().load(quizes.get(position).getIconUrl() + "").into(iv_quizIcon);

        if(quizes.get(position).getActive())
        {
            iv_stateImage.setImageResource(R.drawable.greenmark);

        }else{

            iv_stateImage.setImageResource(R.drawable.redmark);

        }


        return convertView;
    }
}
