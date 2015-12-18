package com.example.brickses2.GameObjects;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.brickses2.Constants.WorldConstants;
import com.example.brickses2.DB.Player;
import com.example.brickses2.GLClasses.GLRenderer;
import com.example.brickses2.Interfaces.IGraphicEntity;
import com.example.brickses2.MainActivity;
import com.example.brickses2.Managers.BufferManager;
import com.example.brickses2.Managers.TextManager;
import com.example.brickses2.MenuActivity;
import com.example.brickses2.Stats.PlayerStats;

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
    private TextObject scoreTextObject;
    private Context context;

    public List<IGraphicEntity> graphicEntities;
    public PlayerStats currentPlayerStats;
    public TextManager textManager;



    private World() {
        InitializeWorld();
    }

    private void InitializeWorld(){

        bricksRowsCount = WorldConstants.COUNT_OF_BRICKS_IN_A_ROW;
        bricksColumnCount = WorldConstants.COUNT_OF_BRICKS_IN_A_COLUMN;

        graphicEntities = new ArrayList<>();
        currentPlayerStats = new PlayerStats();


        InitializePlayer();
        InitializeBall();
        InitializeBricks();
        InitializeBackGround();
        InitializeTextManager();
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

    private void InitializeTextManager() {
        textManager = new TextManager();
        // Tell our text manager to use index 1 of textures loaded
        textManager.setTextureID(4);

        // Pass the uniform scale
        textManager.setUniformscale(1f);

        scoreTextObject = new TextObject("score: " + currentPlayerStats.GetScore(),WorldConstants.TEXT_LEFT_MARGIN, WorldConstants.TEXT_BOTTOM_MARGIN);
        // Add it to our manager
        textManager.addText(scoreTextObject);

        // Prepare the text for rendering
        textManager.PrepareDraw();
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

        vertices.add((float) GLRenderer.screenHeight);
        vertices.add((float) GLRenderer.screenWidth);
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
        textManager.txtcollection.get(textManager.txtcollection.indexOf(scoreTextObject)).text = "score: " + currentPlayerStats.GetScore();
        textManager.PrepareDraw();
    }

    public void MoveObjects(){
        ((PlayerObject)graphicEntities.get(0)).Move();
        ((BallObject)graphicEntities.get(1)).Move();
    }

    public void SetContext(Context mContext)
    {
        this.context = mContext;
    }

    public void GameOver(){
        int _currentHighscore = Integer.parseInt(MenuActivity.currentPlayer.getHighScore());
        if (_currentHighscore < currentPlayerStats.score) {
            MenuActivity.currentPlayer.setHighScore(currentPlayerStats.score);
            MenuActivity.updatePlayer();
        }
        Intent myIntent = new Intent(context, MenuActivity.class);
        context.startActivity(myIntent);
        InitializeWorld();
    }


}
