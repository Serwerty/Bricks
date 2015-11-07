package com.example.brickses2;

import android.graphics.Rect;

import com.example.brickses2.World.World;
import com.example.brickses2.World.WorldConstants;

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
    public short[] indices;
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;
    public int size1;
    
    public void CheckForColision(float X)
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

        for(int i=0;i< WorldConstants.WORLD_COUNT_OF_BRICKS_IN_A_ROW; i++)
        {
            for(int j=0;j< WorldConstants.WORLD_COUNT_OF_BRICKS_IN_A_COLUMN; j++)
            {
               if (World.GetInstance().world[i][j].exsist && World.GetInstance().world[i][j].brick.left>ball.right
                       && World.GetInstance().world[i][j].brick.right<ball.left
                       && World.GetInstance().world[i][j].brick.bottom<ball.top
                       && World.GetInstance().world[i][j].brick.bottom<ball.bottom)
               {
                   ball.top=World.GetInstance().world[i][j].brick.bottom;
                   ball.bottom=World.GetInstance().world[i][j].brick.bottom-50;
                   speedY=-speedY;
                   World.GetInstance().world[i][j].exsist=false;
               }
                else if (World.GetInstance().world[i][j].exsist && World.GetInstance().world[i][j].brick.left>ball.right
                        && World.GetInstance().world[i][j].brick.right<ball.left
                        && World.GetInstance().world[i][j].brick.top>ball.bottom
                        && World.GetInstance().world[i][j].brick.top>ball.bottom)
                {
                    ball.top=World.GetInstance().world[i][j].brick.bottom;
                    ball.bottom=World.GetInstance().world[i][j].brick.bottom-50;
                    speedY=-speedY;
                    World.GetInstance().world[i][j].exsist=false;
                }
                else if (World.GetInstance().world[i][j].exsist && World.GetInstance().world[i][j].brick.top>ball.bottom
                        && World.GetInstance().world[i][j].brick.bottom<ball.top
                        && World.GetInstance().world[i][j].brick.left<ball.right
                        && World.GetInstance().world[i][j].brick.left>ball.left)
                {
                    ball.left=World.GetInstance().world[i][j].brick.right;
                    ball.right=World.GetInstance().world[i][j].brick.right+50;
                    speedX=-speedX;
                    World.GetInstance().world[i][j].exsist=false;
                }
                else if (World.GetInstance().world[i][j].exsist && World.GetInstance().world[i][j].brick.top>ball.bottom
                        && World.GetInstance().world[i][j].brick.bottom<ball.top
                        && World.GetInstance().world[i][j].brick.right>ball.left
                        && World.GetInstance().world[i][j].brick.right>ball.right)
                {
                    ball.left=World.GetInstance().world[i][j].brick.left-50;
                    ball.right=World.GetInstance().world[i][j].brick.left;
                    speedX=-speedX;
                    World.GetInstance().world[i][j].exsist=false;
                }
            }
        }

        if (ball.bottom<0)
        {
            ball.top=50;
            ball.bottom=0;
            speedY=-speedY;
        }
        if (ball.bottom<80 && ball.right>X && ball.left<X+300)
        {
            ball.top=130;
            ball.bottom=80;
            speedY=-speedY;
            speedX=((X+150)-(ball.right+25))*-10/150;
        }
    }
    
    public void MoveBall(float X)
    {

        ball.bottom+=speedY;
        ball.top+=speedY;
        ball.right+=speedX;
        ball.left+=speedX;
        CheckForColision(X);

        vertices = new ArrayList<Float>();
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

        float[] floatArray = new float[vertices.size()];
        int i = 0;

        for (Float f : vertices) {
            floatArray[i++] = (f != null ? f : Float.NaN);
        }

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(floatArray.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(floatArray);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);
    }
}
