package com.example.brickses2;

import android.graphics.Rect;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.List;

/**
 * Created by Oleg Dovzhenko on 07.11.2015.
 */
public class Ball {
    public Ball()
    {
        ball = new Rect();
        ball.left = 720/2-25;
        ball.right = 720/2+25;
        ball.bottom = 80;
        ball.top = 130;
    }

    public Rect ball;
    public float speedX=5;
    public float speedY=10;
    public List<Float> vertices;
    public List<Short> indices;
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;
    public int size1;
    
    public void CheckForColision()
    {
        if (ball.left<=0)
        {
            ball.left=0;
            ball.right=50;
            speedX=-speedX;
        }
        
        if (ball.right>=720)
        {
            ball.left=670;
            ball.right=720;
            speedX=-speedX;
        }
        
        if (ball.top>1280)
        {
            ball.top=1280;
            ball.bottom=1230;
            speedY=-speedY;
        }

        if (ball.bottom<0)
        {
            ball.top=50;
            ball.bottom=0;
            speedY=-speedY;
        }
    }
    
    public void MoveBall()
    {
        ball.bottom+=speedY;
        ball.top+=speedY;
        ball.right+=speedX;
        ball.left+=speedX;
        CheckForColision();
        vertices.add(ball.left * 1f);
        vertices.add(ball.top * 1f);
        vertices.add(0f);

        vertices.add(ball.left * 1f);
        vertices.add(ball.bottom * 1f);
        vertices.add(0f);

        vertices.add(ball.right * 1f);
        vertices.add(ball.bottom * 1f);
        vertices.add(0f);

        vertices.add(ball.right * 1f);
        vertices.add(ball.top * 1f);
        vertices.add(0f);

        indices = new short[] {0, 1, 2, 0, 2, 3 };

    }
}
