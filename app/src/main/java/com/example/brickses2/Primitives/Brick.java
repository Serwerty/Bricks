package com.example.brickses2.Primitives;

import android.graphics.Rect;

import com.example.brickses2.GLRenderer;
import com.example.brickses2.MainActivity;

/**
 * Created by Олег on 06.11.2015.
 */
public class Brick {
    public Brick(int i,int j)
    {
        brick = new Rect();
        brick.left=i*(50+30);
        brick.right=i*(50+30)+50;
        brick.top=1280-j*(50+30);
        brick.bottom=1280-(j*(50+30)+50);
    }



    public Rect brick;
}
