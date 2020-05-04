package com.tansuyegen.quizapp.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tansuyegen.quizapp.Adapters.QuizesAdapter;
import com.tansuyegen.quizapp.Models.Quiz;
import com.tansuyegen.quizapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class QuizesActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    TextView home;
    ListView lv_activeQuizes;
    ArrayList<Quiz> quizes;
    QuizesAdapter quizesAdapter;

    ImageView iv_menuIcon;
    FirebaseAuth mAuth;

    LinearLayout ly_laoding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_quizes);
        home=findViewById(R.id.anas);
        onCreateMethods();

        fetchAllQuizes();

    }

    private void onCreateMethods(){

        ly_laoding = findViewById(R.id.ly_laoding);
        iv_menuIcon = findViewById(R.id.iv_menuIcon);
        iv_menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuDialog();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        quizes = new ArrayList<Quiz>();
        lv_activeQuizes = findViewById(R.id.lv_activeQuizes);
        quizesAdapter = new QuizesAdapter(this,R.layout.item_view_quiz,quizes);
        lv_activeQuizes.setAdapter(quizesAdapter);

        lv_activeQuizes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Quiz clicked_quiz = quizes.get(position);

                if(clicked_quiz.getActive()){

                    Intent i = new Intent(QuizesActivity.this, QuizActivity.class);
                    i.putExtra("clicked_quiz_id",quizes.get(position).getId());
                    startActivity(i);

                }
            }
        });

        //To Change The Status Bar Color and Bottom NavBar Color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        window.setNavigationBarColor(Color.parseColor("#5c4db1"));


    }

    private void showMenuDialog(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_menu);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }



    private void fetchAllQuizes(){

        final Date currentTime = Calendar.getInstance().getTime();
        startLoading();

        db.collection("Quizes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            finishLoading();

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Long end_time = 0L;
                                Long start_time = 0L;
                                String numberOfQuestions;

                                Quiz quiz;

                                end_time = document.getTimestamp("EndTime").getSeconds();
                                start_time = document.getTimestamp("StartTime").getSeconds();
                                numberOfQuestions =  document.getString("NumberOfQuestions");

                                if((dateToTimestamp(currentTime) < end_time) && dateToTimestamp(currentTime) >= start_time){

                                    Log.d("AVAILABLE_QUIZES:" ,  document.getId()+" => " + document.getData());
                                    quiz = new Quiz(document.get("Title") + " ", document.getId() + "" ,start_time,end_time,Integer.parseInt(numberOfQuestions) ,true);

                                }else{
                                    quiz = new Quiz(document.get("Title") + " ",document.getId() + "",start_time,end_time,Integer.parseInt(numberOfQuestions) ,false);
                                    Log.d("AVAILABLE_QUIZES:" ,  "NOT FOUND " + end_time + "-" + dateToTimestamp(currentTime));

                                }

                                quizes.add(quiz);
                                quizesAdapter.notifyDataSetChanged();

                            }
                        } else {
                            Log.d("RESULT", "Error getting documents: ", task.getException());
                        }
                    }
                });



    }

    public static long dateToTimestamp(Date date) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTime(date);
        return cal.getTimeInMillis() / 1000L;
    }

    private void startLoading(){

        LinearLayout ly_laoding = findViewById(R.id.ly_laoding);
        ImageView iv_laoding = findViewById(R.id.iv_loading);

        ly_laoding.setVisibility(View.VISIBLE);
        Animation animShake = AnimationUtils.loadAnimation(this, R.anim.anim_shake);
        iv_laoding.startAnimation(animShake);

    }

    private void finishLoading(){

        LinearLayout ly_laoding = findViewById(R.id.ly_laoding);
        ImageView iv_laoding = findViewById(R.id.iv_loading);
        ly_laoding.setVisibility(View.GONE);
//        Animation animShake = AnimationUtils.loadAnimation(getContext(), R.anim.anim_shake);
//        iv_laoding.startAnimation(animShake);

    }

    public void profil(View view) {
        Intent intent=new Intent(QuizesActivity.this, ProfilActivity.class);
        startActivity(intent);
    }

    public void Cikis1(View view) {
        mAuth.signOut();
        Intent intent=new Intent(QuizesActivity.this,AuthActivity.class);
        startActivity(intent);
        finish();
    }


    public void Anasayfa(View view) {
        Intent intent=new Intent(QuizesActivity.this,QuizesActivity.class);
        startActivity(intent);
        finish();
    }
}