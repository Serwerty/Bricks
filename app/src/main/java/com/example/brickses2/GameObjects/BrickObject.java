package com.example.brickses2.GameObjects;

import android.graphics.Rect;
import com.example.brickses2.Constants.WorldConstants;
import com.example.brickses2.Interfaces.IGraphicEntity;
import com.example.brickses2.Managers.BufferManager;

import java.util.ArrayList;
import java.util.List;


public class BrickObject implements IGraphicEntity {

    private Rect rectangle;

    public short brokeLimit = 1; //FUTURE

    public  BrickObject(short i, short j){

        int left = i * (WorldConstants.BRICK_SIZE + WorldConstants.BRICK_PADDING);
        int top = 1280 - j * (WorldConstants.BRICK_SIZE + WorldConstants.BRICK_PADDING); //TODO
        int right = i * (WorldConstants.BRICK_SIZE + WorldConstants.BRICK_PADDING) + WorldConstants.BRICK_SIZE;
        int bottom = 1280 - (j * (WorldConstants.BRICK_SIZE +  WorldConstants.BRICK_PADDING) + WorldConstants.BRICK_SIZE);

        rectangle = new Rect(left, top, right, bottom);
    }

    @Override
    public void DrawGL() {

        if (brokeLimit > 0) {
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

            BufferManager.GetInstance().BricksBufferCollection.Add(vertices);
        }
    }
}