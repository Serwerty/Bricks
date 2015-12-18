package com.example.brickses2.DB;

/**
 * Created by Олег on 18.12.2015.
 */
public class Player {
    private int id;
    private String name;
    private int highScore;

    public Player(){}

    public Player(String name, int highScore) {
        super();
        this.name = name;
        this.highScore = highScore;
    }

    //getters & setters

    @Override
    public String toString() {
        return "Book [id=" + id + ", name=" + name + ", highScore=" + highScore
                + "]";
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return this.name;
    }

    public String getHighScore()
    {
        return Integer.toString(this.highScore);
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setHighScore(int highScore)
    {
        this.highScore = highScore;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
