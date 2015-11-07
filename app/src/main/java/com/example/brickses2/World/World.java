package com.example.brickses2.World;

import android.os.Build;
import android.support.annotation.NonNull;

import com.example.brickses2.Primitives.Brick;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Олег on 06.11.2015.
 */
public class World {

    private static World Instance;
    private World() {
        this.n = WorldConstants.WORLD_COUNT_OF_BRICKS_IN_A_ROW;
        this.m = WorldConstants.WORLD_COUNT_OF_BRICKS_IN_A_COLUMN;
        world = new Brick[n][m];
        for (int i=0;i<n;i++)
        {
            for (int j=0;j<m;j++)
            {
                world[i][j] = new Brick(i,j);
            }
        }
    }
    public static World GetInstance() {
        if (Instance==null)
            Instance = new World();
        return Instance;
    }

    public Brick[][] world;
    public List<Float> vertices;
    public List<Short> indices;
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;
    public int size1;

    private int n;
    private int m;

    public void DrawWorld()
    {
        short _counter=0;
        vertices = new ArrayList<Float>();
        indices = new ArrayList<Short>();
        for (int i=0;i<n;i++)
        {
            for (int j=0;j<m;j++) {
                if (world[i][j].exsist) {
                    vertices.add(world[i][j].brick.left * 1f);
                    vertices.add(world[i][j].brick.top * 1f);
                    vertices.add(0f);

                    vertices.add(world[i][j].brick.left * 1f);
                    vertices.add(world[i][j].brick.bottom * 1f);
                    vertices.add(0f);

                    vertices.add(world[i][j].brick.right * 1f);
                    vertices.add(world[i][j].brick.bottom * 1f);
                    vertices.add(0f);

                    vertices.add(world[i][j].brick.right * 1f);
                    vertices.add(world[i][j].brick.top * 1f);
                    vertices.add(0f);

                    indices.add(_counter);
                    indices.add((short) (_counter + 1));
                    indices.add((short) (_counter + 2));
                    indices.add(_counter);
                    indices.add((short) (_counter + 2));
                    indices.add((short) (_counter + 3));

                    _counter += 4;
                }
            }
        }

        short[] intArray = new short[indices.size()];
        float[] floatArray = new float[vertices.size()];
        int i = 0;

        for (Float f : vertices) {
            floatArray[i++] = (f != null ? f : Float.NaN);
        }
        i=0;
        for (short f : indices) {
            intArray[i++] = f;
    }
        size1=intArray.length;

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(floatArray.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(floatArray);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(intArray.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(intArray);
        drawListBuffer.position(0);
    }


}
