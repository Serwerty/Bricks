package com.example.brickses2.GameObjects;

import android.graphics.Rect;

import com.example.brickses2.Constants.WorldConstants;
import com.example.brickses2.Interfaces.IGraphicEntity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleg Dovzhenko on 07.11.2015.
 */
public class Ball {

    public Rect ball;
    public float speedX=5;
    public float speedY=10;
    public List<Float> vertices;
    public short[] indices;
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;
    public int size1;
    
    public void CheckForColision(float X)
    {



        for(int i=0;i< WorldConstants.COUNT_OF_BRICKS_IN_A_ROW; i++)
        {
            for(int j=0;j< WorldConstants.COUNT_OF_BRICKS_IN_A_COLUMN; j++)
            {
                if(World.GetInstance().world[i][j].exsist)
                {
                    if (checkCollision(World.GetInstance().world[i][j].brick,ball))
                    {
                        Rect _prevPos = ball;
                        _prevPos.bottom-=speedY;
                        _prevPos.top-=speedY;
                        _prevPos.right-=speedX;
                        _prevPos.left-=speedX;
                        Rect _brick = World.GetInstance().world[i][j].brick;

                        if (_prevPos.top<_brick.bottom)
                        {
                            //Top
                            ball.bottom=World.GetInstance().world[i][j].brick.bottom-50;
                            ball.top=World.GetInstance().world[i][j].brick.bottom;
                            speedY=-speedY;
                        }
                        else if (_prevPos.bottom>_brick.top)
                        {
                            //Bottom
                            ball.bottom=World.GetInstance().world[i][j].brick.top;
                            ball.top=World.GetInstance().world[i][j].brick.top+50;
                            speedY=-speedY;
                        }
                        else if (_prevPos.left>_brick.right)
                        {
                            //Left
                            ball.left=World.GetInstance().world[i][j].brick.right;
                            ball.right=World.GetInstance().world[i][j].brick.right+50;
                            speedX=-speedX;
                        }
                        else if (_prevPos.right<_brick.left)
                        {
                            //Right
                            ball.left=World.GetInstance().world[i][j].brick.left-50;
                            ball.right=World.GetInstance().world[i][j].brick.left;
                            speedX=-speedX;
                        }
                        else
                        {
                            speedX=-speedX;
                            speedY=-speedY;
                        }
                        World.GetInstance().world[i][j].exsist=false;
                    }
                }
            }
        }


    }

    private boolean checkCollision(Rect a, Rect b)
    {
        boolean collisionX = a.right >= b.left &&
                b.right >= a.left;
        // Collision y-axis?
        boolean collisionY = a.top>= b.bottom &&
                b.top >= a.bottom;
        // Collision only if on both axes
        return collisionX && collisionY;
    }

    public void MoveBall(float X)
    {

        ball.bottom+=speedY;
        ball.top+=speedY;
        ball.right+=speedX;
        ball.left+=speedX;
        CheckForColision(X);

    }
}
