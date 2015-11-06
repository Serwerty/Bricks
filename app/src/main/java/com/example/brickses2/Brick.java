package com.example.brickses2;

import android.graphics.Rect;

/**
 * Created by Олег on 06.11.2015.
 */
public class Brick {
    public Brick(int i,int j)
    {
        brick = new Rect();
        brick.left=i*(50+3);
        brick.right=i*(50+3)+50;
        brick.top=j*(50+3);
        brick.bottom=j*(50+3)+50;
    }



    public Rect brick;
}
