package com.tansuyegen.quizapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tansuyegen.quizapp.Models.Answer;
import com.tansuyegen.quizapp.R;

import java.util.ArrayList;
import java.util.List;

public class AnswersAdapter extends ArrayAdapter<Answer> {


    LayoutInflater inflater;
    Context context;
    ArrayList<Answer> answers;



    public AnswersAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Answer> objects) {
        super(context, resource, objects);

        this.context = context;
        inflater  = LayoutInflater.from(context);
        this.answers = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_view_answer,null);

        TextView tv_answerDesc = convertView.findViewById(R.id.tv_answerDesc);
        TextView tv_answerLetter = convertView.findViewById(R.id.tv_answerLetter);

        String a_letter = answers.get(position).getLetter();
        String a_desc = answers.get(position).getDesc();

        tv_answerDesc.setText(a_desc);
        tv_answerLetter.setText(a_letter);

        return convertView;
    }
}
