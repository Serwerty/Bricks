package com.example.brickses2.GameObjects;

import android.graphics.Rect;

import com.example.brickses2.Constants.WorldConstants;
import com.example.brickses2.GLClasses.GLRenderer;
import com.example.brickses2.Interfaces.IGraphicEntity;
import com.example.brickses2.Managers.BufferManager;

import java.util.ArrayList;
import java.util.List;


public class World {

    //Singletone instance
    private static World Instance;

    public static World GetInstance() {
        if (Instance == null)
            Instance = new World();
        return Instance;
    }

    private int bricksRowsCount;
    private int bricksColumnCount;

    public List<IGraphicEntity> graphicEntities;

    private World() {

        bricksRowsCount = WorldConstants.COUNT_OF_BRICKS_IN_A_ROW;
        bricksColumnCount = WorldConstants.COUNT_OF_BRICKS_IN_A_COLUMN;

        graphicEntities = new ArrayList<>();

        InitializePlayer();
        InitializeBall();
        InitializeBricks();
        InitializeBackGround();
    }

    private void InitializeBricks(){
        for(short i = 0; i < bricksRowsCount; i++){
            for(short j = 0; j < bricksColumnCount; j++){
                BrickObject gameObject = new BrickObject(i, j);
                graphicEntities.add(gameObject);
            }
        }
    }

    private void InitializePlayer(){
        PlayerObject player =  new PlayerObject();
        graphicEntities.add(player);
    }

    private void InitializeBall(){
        BallObject ball = new BallObject();
        graphicEntities.add(ball);
    }

    private void InitializeBackGround(){
        List<Float> vertices = new ArrayList<Float>();
        vertices.add(0f);
        vertices.add((float)GLRenderer.screenWidth);
        vertices.add(0f);

        vertices.add(0f);
        vertices.add(0f);
        vertices.add(0f);

        vertices.add((float)GLRenderer.screenHeight);
        vertices.add(0f);
        vertices.add(0f);

        vertices.add((float)GLRenderer.screenHeight);
        vertices.add((float)GLRenderer.screenWidth);
        vertices.add(0f);

        BufferManager.GetInstance().BackGroundBufferCollection.Add(vertices);
        BufferManager.GetInstance().BackGroundBufferCollection.FillBuffer();
    }

    public void DrawWorld() {
        BufferManager.GetInstance().PlayerBufferCollection.ClearBuffer();
        BufferManager.GetInstance().BallBufferCollection.ClearBuffer();
        BufferManager.GetInstance().BricksBufferCollection.ClearBuffer();
       // BufferManager.GetInstance().BackGroundBufferCollection.ClearBuffer();
        for (IGraphicEntity entity :  graphicEntities) {
            entity.DrawGL();
        }
        BufferManager.GetInstance().PlayerBufferCollection.FillBuffer();
        BufferManager.GetInstance().BallBufferCollection.FillBuffer();
        BufferManager.GetInstance().BricksBufferCollection.FillBuffer();

    }

    public void MoveObjects(){
        ((PlayerObject)graphicEntities.get(0)).Move();
        ((BallObject)graphicEntities.get(1)).Move();
    }
}
