package com.example.velimatti.labyrinthleaderboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private int league = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            league = savedInstanceState.getInt("LEAGUE");
        }
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt( "LEAGUE", league );
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        league = savedInstanceState.getInt("LEAGUE");
    }

    public void btn1Click(View v) {
        league = 1;
        onClick();
    }
    public void btn2Click(View v) {
        league = 2;
        onClick();
    }
    public void btn3Click(View v) {
        league = 3;
        onClick();
    }
    public void btn4Click(View v) {
        league = 4;
        onClick();
    }
    public void btn5Click(View v) {
        finishAndRemoveTask();
    }

    public void onClick() {
        // Siirrytään Leaderboard -näkymään
        Intent i = new Intent(this, LeaderboardActivity.class);
        i.putExtra("LEAGUE", league);
        startActivity(i);
    }
}
