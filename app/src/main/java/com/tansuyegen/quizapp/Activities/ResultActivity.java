package com.tansuyegen.quizapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

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
import com.tansuyegen.quizapp.Adapters.LeaderBoardAdapter;
import com.tansuyegen.quizapp.Models.LeaderBoardUser;
import com.tansuyegen.quizapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ListView ly_leaderBoard;
    LeaderBoardAdapter leaderBoardAdapter;
    ArrayList<LeaderBoardUser> usersLeaderBoard;

    TextView tv_first_name,tv_second_name,tv_third_name;
    TextView tv_first_point,tv_second_point,tv_third_point;

    int leaderBoardOrdering = 0;
    int currentUserPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        onCreateMethods();
//        fillUsersLeaderBoard();

        String leaderBoardId = getIntent().getExtras().getString("leaderBoardId");
        int score = getIntent().getExtras().getInt("score");
        fetchLeaderBoardAndSaveCurrent(leaderBoardId, score);
    }

//Sorun değil bi'tanem
    //Seni çok sevdim, söyleyemedim...
        //Sen bunu bilmedin....

    private void fetchLeaderBoardAndSaveCurrent(String leaderBoardId, final int score){

        final DocumentReference docRef = db.collection("LeaderBoards").document(leaderBoardId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());

                        ArrayList<Map<String,Integer>> scores = (ArrayList<Map<String, Integer>>) document.get("Scores");
                        Log.i("ScoresArray","" + scores.get(0));

                        String str_scores = scores.toString();
                        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;


                        if(str_scores.contains(currentFirebaseUser.getUid())){
                            //Already solved, there is already result

                            Log.i("TEST_IS_TAKEN_BEFORE","YES!");

                        }

                        Map<String,Integer> currentUserScore = new HashMap<>();
                        currentUserScore.put(currentFirebaseUser.getUid(),score);
                        scores.add(currentUserScore);

                        for( Map<String,Integer> user_score : scores){

                            for ( String key : user_score.keySet() ) {


                                String point = user_score.get(key) + "";
                                fetchUser(key, point);


                            }

                        }

                        docRef.update("Scores", scores).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully updated!");



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error updating document", e);
                            }
                        });

                        //To update


                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

    }

    private void fetchUser(final String uId, final String point){

        DocumentReference docRef = db.collection("Users").document(uId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();

                    String nickname = document.get("nickname") + "";

                    if(nickname.equals("null"))
                        nickname  = "Anonim Kullanıcı";

                    fillUsersLeaderBoard(uId,point,nickname);



                } else {
                }
            }
        });

    }

    private void onCreateMethods(){

        tv_first_name = findViewById(R.id.tv_first_name);
        tv_second_name = findViewById(R.id.tv_second_name);
        tv_third_name = findViewById(R.id.tv_third_name);
        tv_first_point = findViewById(R.id.tv_first_point);
        tv_second_point = findViewById(R.id.tv_second_point);
        tv_third_point = findViewById(R.id.tv_third_point);

        usersLeaderBoard = new ArrayList<>();
        ly_leaderBoard = findViewById(R.id.ly_leaderBoard);
        leaderBoardAdapter = new LeaderBoardAdapter(this, R.layout.item_view_leaderboard,usersLeaderBoard);
        ly_leaderBoard.setAdapter(leaderBoardAdapter);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        window.setNavigationBarColor(Color.parseColor("#5c4db1"));

    }

    private void fillUsersLeaderBoard(String userId, String userPoint, String userNickname){

        usersLeaderBoard.add(new LeaderBoardUser(userId,0,Integer.parseInt(userPoint),userNickname,"url"));


        Boolean sorted = false;
        while(!sorted) {
            sorted = true;

        for(int i = 0 ; i < usersLeaderBoard.size() - 1 ; i++){
            int pointP = usersLeaderBoard.get(i).getUser_point();
            int pointN = usersLeaderBoard.get(i + 1).getUser_point();
                if (pointN < pointP) {
                    LeaderBoardUser leaderBoardUser_temp = usersLeaderBoard.get(i); //7
                    usersLeaderBoard.set(i, usersLeaderBoard.get(i + 1)); //5
                    usersLeaderBoard.set((i + 1), leaderBoardUser_temp);
                    sorted = false;

                }
            }



        }


        Collections.reverse(usersLeaderBoard);
        leaderBoardAdapter.notifyDataSetChanged();

        for(int i = 0 ; i < usersLeaderBoard.size() - 1 ; i++){

            usersLeaderBoard.get(i).setUser_order_no(i + 1);

            if(usersLeaderBoard.get(i).getUserId().equals(userId)){

                ly_leaderBoard.smoothScrollToPosition(i);

            }


        }


       if(usersLeaderBoard.size() > 2) fillTopThree( usersLeaderBoard.get(0),usersLeaderBoard.get(1),usersLeaderBoard.get(2));

    }

    private void fillTopThree(LeaderBoardUser first, LeaderBoardUser second, LeaderBoardUser third){

        String first_name = first.getUser_nickname();
        String second_name = second.getUser_nickname();
        String third_name = third.getUser_nickname();

        int first_point = first.getUser_point();
        int second_point = second.getUser_point();
        int third_point = third.getUser_point();

        tv_first_name.setText(first_name);
        tv_second_name.setText(second_name);
        tv_third_name.setText(third_name);

        tv_first_point.setText(first_point +" Puan");
        tv_second_point.setText(second_point + " Puan");
        tv_third_point.setText(third_point + " Puan");


    }

}
