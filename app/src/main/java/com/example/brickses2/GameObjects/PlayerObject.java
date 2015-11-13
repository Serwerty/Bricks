package com.example.brickses2.GameObjects;

import android.graphics.Rect;

import com.example.brickses2.Constants.WorldConstants;
import com.example.brickses2.Interfaces.IGraphicEntity;
import com.example.brickses2.Managers.BufferManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;


public class PlayerObject implements IGraphicEntity {

    private Rect rectangle;

    public  BrickObject(short i, short j){

        int left = i * (WorldConstants.BRICK_SIZE + WorldConstants.BRICK_PADDING);
        int top = 1280 - j * (WorldConstants.BRICK_SIZE + WorldConstants.BRICK_PADDING); //TODO
        int right = i * (WorldConstants.BRICK_SIZE + WorldConstants.BRICK_PADDING) + WorldConstants.BRICK_SIZE;
        int bottom = 1280 - (j * (WorldConstants.BRICK_SIZE +  WorldConstants.BRICK_PADDING) + WorldConstants.BRICK_SIZE);

        rectangle = new Rect(left, top, right, bottom);
    }
OI
    @Override
    public void DrawGL() {

        List<Float> vertices = new ArrayList<>();
        List<Short> indices = new ArrayList<>();

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

        indices.add(indexCount);
        indices.add((short) (indexCount + 1));
        indices.add((short) (indexCount + 2));
        indices.add(indexCount);
        indices.add((short) (indexCount + 2));
        indices.add((short) (indexCount + 3));

        BufferManager.GetInstance().PlayerBufferCollection.FillBuffer();
    }
}
