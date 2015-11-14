package com.example.brickses2.GameObjects;

import android.graphics.Rect;

import com.example.brickses2.Constants.WorldConstants;
import com.example.brickses2.Interfaces.IGraphicEntity;
import com.example.brickses2.Interfaces.IMovable;
import com.example.brickses2.Managers.BufferManager;

import java.util.ArrayList;
import java.util.List;

public class BallObject implements IGraphicEntity, IMovable {

    private Rect rectangle;
    private int velocityX;
    private int velocityY;

    public  BallObject(){
        rectangle = new Rect();
        rectangle.left = 720 / 2 - WorldConstants.BALL_SIZE;
        rectangle.right = 720 / 2 + WorldConstants.BALL_SIZE;
        rectangle.bottom = WorldConstants.PLAYER_HEIGHT;
        rectangle.top = WorldConstants.PLAYER_HEIGHT + WorldConstants.BALL_SIZE;
    }

    @Override
    public void DrawGL() {

        List<Float> vertices = new ArrayList<>();
        vertices.add((float)rectangle.left);
        vertices.add((float)rectangle.top);
        vertices.add(0f);

        vertices.add((float)rectangle.left);
        vertices.add((float)rectangle.bottom);
        vertices.add(0f);

        vertices.add((float)rectangle.right);
        vertices.add((float)rectangle.bottom);
        vertices.add(0f);

        vertices.add((float)rectangle.right);
        vertices.add((float)rectangle.top);
        vertices.add(0f);

        BufferManager.GetInstance().BallBufferCollection.Add(vertices);
    }

    @Override
    public void Move() {
        rectangle.offset(velocityX, velocityY);

        CheckCollision();
    }

    private void CheckCollision(){

        //TODO: Fix screen size
        if (rectangle.left <= 0) {
            rectangle.left = 0;
            rectangle.right = WorldConstants.BALL_SIZE;
            velocityX =- velocityX;
        }

        if (rectangle.right >= 720) {
            rectangle.left = 720 - WorldConstants.BALL_SIZE;
            rectangle.right = 720;
            velocityX =- velocityX;
        }

        if (rectangle.top > 1280) {
            rectangle.top = 1280;
            rectangle.bottom = 1280 - WorldConstants.BALL_SIZE;
            velocityY =- velocityY;
        }

        if (rectangle.bottom < 0) {
            rectangle.top = 50;
            rectangle.bottom = 0;
            velocityY =- velocityY;
        }

        if (rectangle.bottom < 80 && rectangle.right > X && rectangle.left < X + WorldConstants.PLAYER_WIDTH) {
            ball.top=130;
            ball.bottom=80;
            if (speedY<0)
                speedY=-speedY;
            speedX=((X+150)-(ball.right+25))*-10/150;
        }

        for(IGraphicEntity entity : World.GetInstance().graphicEntities){
            if(entity instanceof BrickObject){
                BrickObject brick = (BrickObject)entity;
                if(brick.Exists()){
                    if(rectangle.intersect(brick.rectangle)){
                        Rect _prevPos = rectangle;
                        _prevPos.offset(-velocityX, -velocityY);

                        if (_prevPos.top < brick.rectangle.bottom) {
                            //Top
                            rectangle.bottom =  brick.rectangle.bottom - WorldConstants.BRICK_SIZE;
                            rectangle.top = brick.rectangle.bottom;
                            if(velocityY > 0) velocityY =- velocityY;
                        }
                        else if (_prevPos.bottom > brick.rectangle.top) {
                            //Bottom
                            rectangle.bottom = brick.rectangle.top;
                            rectangle.top = brick.rectangle.top + WorldConstants.BRICK_SIZE;
                            if(velocityY < 0) velocityY =- velocityY;
                        }
                        else if (_prevPos.left > brick.rectangle.right) {
                            //Left
                            rectangle.left = brick.rectangle.right;
                            rectangle.right = brick.rectangle.right + WorldConstants.BRICK_SIZE;
                            if(velocityX < 0) velocityX =- velocityX;
                        }
                        else if (_prevPos.right < brick.rectangle.left) {
                            //Right
                            rectangle.left = brick.rectangle.left - WorldConstants.BRICK_SIZE;
                            rectangle.right = brick.rectangle.left;
                            if(velocityX > 0) velocityX =- velocityX;
                        }

                        brick.Break();
                    }
                }
            }
        }

    }
}
