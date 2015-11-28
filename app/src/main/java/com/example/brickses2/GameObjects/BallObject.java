package com.example.brickses2.GameObjects;

import android.graphics.Rect;

import com.example.brickses2.Constants.WorldConstants;
import com.example.brickses2.GLClasses.GLRenderer;
import com.example.brickses2.Interfaces.IGraphicEntity;
import com.example.brickses2.Interfaces.IMovable;
import com.example.brickses2.Managers.BufferManager;

import java.util.ArrayList;
import java.util.List;

public class BallObject implements IGraphicEntity, IMovable {

    private Rect rectangle;
    private int velocityX = 5;
    private int velocityY = 10;

    public  BallObject(){
        rectangle = new Rect();
        rectangle.left = GLRenderer.screenHeight / 2 - WorldConstants.BALL_SIZE / 2;
        rectangle.right = GLRenderer.screenHeight / 2 + WorldConstants.BALL_SIZE / 2;
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

        if (rectangle.left <= 0) {
            rectangle.left = 0;
            rectangle.right = WorldConstants.BALL_SIZE;
            velocityX =- velocityX;
        }

        if (rectangle.right >= GLRenderer.screenWidth) {
            rectangle.left = GLRenderer.screenWidth - WorldConstants.BALL_SIZE;
            rectangle.right = GLRenderer.screenWidth;
            velocityX =- velocityX;
        }

        if (rectangle.top > GLRenderer.screenHeight) {
            rectangle.top = GLRenderer.screenHeight;
            rectangle.bottom = GLRenderer.screenHeight - WorldConstants.BALL_SIZE;
            velocityY =- velocityY;
        }

        if (rectangle.bottom < 0) {
            rectangle.top = WorldConstants.BALL_SIZE;
            rectangle.bottom = 0;
            velocityY =- velocityY;
        }

        PlayerObject player = (PlayerObject)World.GetInstance().graphicEntities.get(0); //Player always at first position
        if (rectangle.bottom < WorldConstants.PLAYER_HEIGHT + WorldConstants.PLAYER_BOTTOM_PADDING
                && (rectangle.right > player.rectangle.left
                && rectangle.left < player.rectangle.left + WorldConstants.PLAYER_WIDTH)) {
            rectangle.top = WorldConstants.PLAYER_HEIGHT + WorldConstants.PLAYER_BOTTOM_PADDING + WorldConstants.BALL_SIZE;
            rectangle.bottom = WorldConstants.PLAYER_HEIGHT + WorldConstants.PLAYER_BOTTOM_PADDING;
            if (velocityY < 0) velocityY = -velocityY;

            velocityX = (int)(((player.rectangle.left + WorldConstants.PLAYER_WIDTH / 2) -
                    (rectangle.right + WorldConstants.BALL_SIZE  / 2)) *
                    - ((float)WorldConstants.MAX_BALL_SPEED) / (WorldConstants.PLAYER_WIDTH / 2));
        }

        for(IGraphicEntity entity : World.GetInstance().graphicEntities){
            if(entity instanceof BrickObject){
                BrickObject brick = (BrickObject)entity;
                if(brick.Exists()){

                    if(checkIntersection(rectangle,brick.rectangle)) {

                        Rect _prevPos = rectangle;
                        _prevPos.offset(-velocityX, -velocityY);

                        if (_prevPos.top < brick.rectangle.bottom) {
                            //Top
                            rectangle.bottom =  brick.rectangle.bottom - WorldConstants.BALL_SIZE;
                            rectangle.top = brick.rectangle.bottom;
                            if(velocityY > 0) velocityY =- velocityY;
                        }
                        else if (_prevPos.bottom > brick.rectangle.top) {
                            //Bottom
                            rectangle.bottom = brick.rectangle.top;
                            rectangle.top = brick.rectangle.top + WorldConstants.BALL_SIZE;
                            if(velocityY < 0) velocityY =- velocityY;
                        }
                        else if (_prevPos.left > brick.rectangle.right) {
                            //Left
                            rectangle.left = brick.rectangle.right;
                            rectangle.right = brick.rectangle.right + WorldConstants.BALL_SIZE;
                            if(velocityX < 0) velocityX =- velocityX;
                        }
                        else if (_prevPos.right < brick.rectangle.left) {
                            //Right
                            rectangle.left = brick.rectangle.left - WorldConstants.BALL_SIZE;
                            rectangle.right = brick.rectangle.left;
                            if(velocityX > 0) velocityX =- velocityX;
                        }

                        brick.Break();
                    }
                }
            }
        }
    }

    private boolean checkIntersection(Rect a, Rect b) {
        boolean collisionX = a.right >= b.left &&
                b.right >= a.left;
        // Collision y-axis?
        boolean collisionY = a.top>= b.bottom &&
                b.top >= a.bottom;
        // Collision only if on both axes
        return collisionX && collisionY;
    }
}
