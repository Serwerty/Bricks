package com.example.brickses2.Managers;

import android.support.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class BufferCollection{

    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;
    public int indicesCount;

    private List<Float> verticesList = new ArrayList<>();
    private List<Short> indicesList = new ArrayList<>();
    private short indexCount = 0;

    public void Add(List<Float> vertices) {
        verticesList.addAll(vertices);

        indicesList.add(indexCount);
        indicesList.add((short) (indexCount + 1));
        indicesList.add((short) (indexCount + 2));
        indicesList.add(indexCount);
        indicesList.add((short) (indexCount + 2));
        indicesList.add((short) (indexCount + 3));

        indexCount += 4;
    }

    public void ClearBuffer() {
        verticesList = new ArrayList<>();
        indicesList = new ArrayList<>();
    }

    public void FillBuffer() {

        float[] floatArray = new float[verticesList.size()];
        int i = 0;
        for (Float f : verticesList) {
            floatArray[i++] = (f != null ? f : Float.NaN);
        }

        short[] indicesArray = new short[indicesList.size()];
        i = 0;
        for (Short f : indicesList) {
            indicesArray[i++] = f;
        }

        ByteBuffer bb = ByteBuffer.allocateDirect(floatArray.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(floatArray);
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(indicesArray.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indicesArray);
        drawListBuffer.position(0);

        indicesCount = indicesList.size();
    }
}
