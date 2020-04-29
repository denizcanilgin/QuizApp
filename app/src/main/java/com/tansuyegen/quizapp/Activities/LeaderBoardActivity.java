package com.tansuyegen.quizapp.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.tansuyegen.quizapp.Adapters.LeaderBoardAdapter;
import com.tansuyegen.quizapp.Models.LeaderBoardUser;
import com.tansuyegen.quizapp.R;

import java.util.ArrayList;

public class LeaderBoardActivity extends AppCompatActivity {

    ListView ly_leaderBoard;
    LeaderBoardAdapter leaderBoardAdapter;
    ArrayList<LeaderBoardUser> usersLeaderBoard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        onCreateMethods();
//        fillUsersLeaderBoard();
        leaderBoardAdapter.notifyDataSetChanged();

    }


    private void onCreateMethods(){

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

    private void fillUsersLeaderBoard(){

//        usersLeaderBoard.add(new LeaderBoardUser(4,150,"denizcanilgin","url"));
//        usersLeaderBoard.add(new LeaderBoardUser(5,125,"ulukanulutas","url"));
//        usersLeaderBoard.add(new LeaderBoardUser(6,100,"eminenuralozturk","url"));
//        usersLeaderBoard.add(new LeaderBoardUser(7,75,"cemtuncelli","url"));
//        usersLeaderBoard.add(new LeaderBoardUser(8,50,"mertkaderoglu","url"));

    }

}
