package com.example.brickses2.GameObjects;

import android.graphics.Rect;

import com.example.brickses2.Constants.WorldConstants;
import com.example.brickses2.GLClasses.GLRenderer;
import com.example.brickses2.Interfaces.IGraphicEntity;
import com.example.brickses2.Interfaces.IMovable;
import com.example.brickses2.Managers.BufferManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;


public class PlayerObject implements IGraphicEntity, IMovable {

    public Rect rectangle;

    public  PlayerObject(){

        int left = GLRenderer.screenHeight / 2 - WorldConstants.PLAYER_WIDTH / 2;
        int top = WorldConstants.PLAYER_HEIGHT;
        int right = (GLRenderer.screenHeight + WorldConstants.PLAYER_WIDTH) / 2;
        int bottom = 0;

        rectangle = new Rect(left, top, right, bottom);
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

        BufferManager.GetInstance().PlayerBufferCollection.Add(vertices);
    }

    @Override
    public void Move() {

    }
}
