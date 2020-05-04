package com.tansuyegen.quizapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.tansuyegen.quizapp.Adapters.AnswersAdapter;
import com.tansuyegen.quizapp.Adapters.LeaderBoardAdapter;
import com.tansuyegen.quizapp.Models.Answer;
import com.tansuyegen.quizapp.R;
import com.tansuyegen.quizapp.ServerTime;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class QuizActivity extends AppCompatActivity {

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView tv_timer,tv_questionTitle;
    ListView lv_answers;
    int sec = 90; //Time of quiz

    ArrayList<String> question_ids;

    int current_question = 0;
    int number_of_questions = 0;
    int correct_answer_of_current_question = 0;

    int numberOfCorrectlyAnsweredQuestion = 0;

    String currentQuizId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        onCreateMethods();
//        fetchAllQuizes();

        String quiz_id = getIntent().getExtras().getString("clicked_quiz_id");
        fetchQuiz(quiz_id);
        this.currentQuizId = quiz_id;


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

    private void fetchQuiz(String id){

        DocumentReference docRef = db.collection("Quizes").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());

                        String question_pool_id = document.getString("QuestionPool_ID");
                        fetchQuestionIDs(question_pool_id);

                    } else {
                        Log.d("", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

    }

    private void fetchQuestionIDs(final String question_pool_id){

        DocumentReference docRef = db.collection("QuestionPools").document(question_pool_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        question_ids = (ArrayList<String>) document.get("Questions");
                        Log.d("TAG", "DocumentSnapshot data: " + question_ids.size());
                        fetchQuestion(question_ids.get(current_question));

                    } else {
                        Log.d("", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

    }

    private void fetchQuestion(final String question_position){

        final DocumentReference docRef = db.collection("Questions").document(question_position);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String title = document.getString("Title");
                        String correct_answer = document.getString("CorrectAnswer");
                        correct_answer_of_current_question = Integer.parseInt(correct_answer);
                        ArrayList<String> answers = (ArrayList<String>) document.get("Answers");

                        ArrayList<Answer> answers_object = new ArrayList<>();

                        for(int i = 0 ; i < answers.size() ; i++){

                            answers_object.add(new Answer(i + 1 + ")",  answers.get(i) + ""));

                        }

                        fillQuestionAndAnswers(title,answers_object,correct_answer);

                    } else {
                        Log.d("", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

    }

    private void fillQuestionAndAnswers(String title, ArrayList<Answer> answers, final String correct_answer){

        tv_questionTitle.setText(title);

        AnswersAdapter answersAdapter = new AnswersAdapter(this, R.layout.item_view_answer,answers);
        lv_answers.setAdapter(answersAdapter);
        answersAdapter.notifyDataSetChanged();

        lv_answers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String pos = position + "";

                if(pos.equals(correct_answer)){

                    Log.i("CORRECTNESS","true");
                    numberOfCorrectlyAnsweredQuestion++;

                }else{

                    Log.i("CORRECTNESS","false");


                }

                if(question_ids.size()-1 > current_question) {
                    current_question++;
                    fetchQuestion(question_ids.get(current_question));
                }else{

                    //Test is completed

                    Log.i("SEC","" + sec);

                    int score = sec*correct_answer_of_current_question;
//                    saveScore(score);
                    Intent i = new Intent(QuizActivity.this,ResultActivity.class);
                    i.putExtra("leaderBoardId", "lb_" + currentQuizId);
                    i.putExtra("score", score);
                    startActivity(i);


                }
            }
        });

    }

    private void saveScore(int score){

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        Map<String, Object> score_map = new HashMap<>();
        score_map.put(currentFirebaseUser.getUid() + "", score);

        db.collection("LeaderBoards").document("lb_"+ currentQuizId)
                .set(score_map, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");

                        Intent i = new Intent(QuizActivity.this,ResultActivity.class);
                        i.putExtra("leaderBoardId", "lb_" + currentQuizId);
                        startActivity(i);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }
                });

    }

    private void setNewQuestionAnswers(){





    }

    private void onCreateMethods(){

        // Access a Cloud Firestore instance from your Activity

        question_ids = new ArrayList<>();

        tv_timer = findViewById(R.id.tv_timer);
        lv_answers = findViewById(R.id.lv_answers);
        tv_questionTitle = findViewById(R.id.tv_questionTitle);

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
//        setNewQuestionAnswers(answers); //call this method by array of answers

    }

    public void getServerTime(final ServerTime onComplete) {
        FirebaseFunctions.getInstance().getHttpsCallable("getTime")
                .call()
                .addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
                    @Override
                    public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                        if (task.isSuccessful()) {
                            long timestamp = (long) task.getResult().getData();
                            if (onComplete != null) {
                                onComplete.onSuccess(timestamp);
                            }
                        } else {
                            onComplete.onFailed();
                        }
                    }
                });

    }

    public static long dateToTimestamp(Date date) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTime(date);
        return cal.getTimeInMillis() / 1000L;
    }


}