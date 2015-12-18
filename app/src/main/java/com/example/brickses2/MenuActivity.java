package com.example.brickses2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.example.brickses2.DB.MySQLiteHelper;
import com.example.brickses2.DB.Player;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private static MySQLiteHelper DB;
    List<Player> players;
    public static Player currentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu1);
        DB = new MySQLiteHelper(this);
        //InitDB();

        players = DB.getAllPlayers();
        ((RadioButton)findViewById(R.id.radioButton1)).setText(players.get(0).getName() + " - " + players.get(0).getHighScore());
        ((RadioButton)findViewById(R.id.radioButton2)).setText(players.get(1).getName() + " - " + players.get(1).getHighScore());
        ((RadioButton)findViewById(R.id.radioButton3)).setText(players.get(2).getName() + " - " + players.get(2).getHighScore());
        currentPlayer = players.get(0);
    }

    @Override
    protected void onPostResume(){
        super.onPostResume();
        ((RadioButton)findViewById(R.id.radioButton1)).setText(players.get(0).getName() + " - " + players.get(0).getHighScore());
        ((RadioButton)findViewById(R.id.radioButton2)).setText(players.get(1).getName() + " - " + players.get(1).getHighScore());
        ((RadioButton)findViewById(R.id.radioButton3)).setText(players.get(2).getName() + " - " + players.get(2).getHighScore());
    }

    public void ButtonStartClick(View view)
    {
        Intent myIntent = new Intent(MenuActivity.this, MainActivity.class);

        MenuActivity.this.startActivity(myIntent);
    }

    private void InitDB()
    {
        DB.deleteAllPlayers();

        Player player1 = new Player("Player1",0);
        Player player2 = new Player("Player2",0);
        Player player3 = new Player("Player3",0);

        DB.addPlayer(player1);
        DB.addPlayer(player2);
        DB.addPlayer(player3);
    }


    public void radioButton1Click(View view)
    {
        currentPlayer = players.get(0);
    }

    public void radioButton2Click(View view)
    {
        currentPlayer = players.get(1);
    }

    public void radioButton3Click(View view)
    {
        currentPlayer = players.get(2);
    }

    public static void updatePlayer() {
        DB.updatePlayer(currentPlayer);
    }
}
