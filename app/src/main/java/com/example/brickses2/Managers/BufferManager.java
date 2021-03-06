package com.example.brickses2.Managers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.List;

public class BufferManager {

    //Singletone instance
    private static BufferManager Instance;
    public static BufferManager GetInstance() {
        if (Instance == null)
            Instance = new BufferManager();
        return Instance;
    }

    private BufferManager(){
        BricksBufferCollection = new BufferCollection();
        PlayerBufferCollection = new BufferCollection();
        BallBufferCollection = new BufferCollection();
        BackGroundBufferCollection = new BufferCollection();
    }

    public BufferCollection BricksBufferCollection;
    public BufferCollection PlayerBufferCollection;
    public BufferCollection BallBufferCollection;
    public BufferCollection BackGroundBufferCollection;

}
