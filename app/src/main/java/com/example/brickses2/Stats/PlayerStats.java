package com.example.brickses2.Stats;

/**
 * Created by Олег on 14.12.2015.
 */
public class PlayerStats {
    public String name;
    public int score = 0;

    public PlayerStats() {
    }

    public String GetScore() {
        return Integer.toString(score);
    }

    public void IncrementScore() {
        score++;
    }
}
