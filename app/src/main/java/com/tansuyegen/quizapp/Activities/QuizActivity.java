package com.tansuyegen.quizapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.tansuyegen.quizapp.Adapters.AnswersAdapter;
import com.tansuyegen.quizapp.Adapters.LeaderBoardAdapter;
import com.tansuyegen.quizapp.Models.Answer;
import com.tansuyegen.quizapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class QuizActivity extends AppCompatActivity {

    TextView tv_timer;
    ListView lv_answers;
    int sec = 30; //Time of quiz

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tv_timer = findViewById(R.id.tv_timer);
        lv_answers = findViewById(R.id.lv_answers);

        tv_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(QuizActivity.this, LeaderBoardActivity.class);
                startActivity(i);

            }
        });

        //To Change The Status Bar Color and Bottom NavBar Color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        window.setNavigationBarColor(Color.parseColor("#5c4db1"));

        startTimer();

        ArrayList<Answer> answers = new ArrayList<>();
        setNewQuestionAnswers(answers); //call this method by array of answers


    }

    private  void startTimer(){
        final int fullTime = sec;

        Timer T=new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if(sec > 0 ) {

                    sec = sec - 1;
                    tv_timer.setText(sec + "");

                    setColorOfTimer(fullTime, sec);
                }else{

                    return;

                }
            }
        }, 1000, 1000);

    }

    private void setColorOfTimer(int fullTime, int currentTime){

        int breakPoint = fullTime/3;

        if(fullTime - currentTime < breakPoint)
            tv_timer.setBackgroundResource(R.drawable.greentimer);
        else if(((fullTime - currentTime) >= breakPoint) && ((fullTime - currentTime) < 2 * breakPoint))
            tv_timer.setBackgroundResource(R.drawable.yellowtimer);
        else
            tv_timer.setBackgroundResource(R.drawable.redtimer);


    }

    private void setNewQuestionAnswers(ArrayList<Answer> answers ){


        answers.add(new Answer("A)", "Yanıt 1"));
        answers.add(new Answer("B)", "Yanıt 2"));
        answers.add(new Answer("C)", "Yanıt 3"));
//        answers.add(new Answer("D)", "Yanıt 4"));

        AnswersAdapter answersAdapter = new AnswersAdapter(this, R.layout.item_view_answer,answers);
        lv_answers.setAdapter(answersAdapter);
        answersAdapter.notifyDataSetChanged();


    }



}
