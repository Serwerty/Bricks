package com.example.brickses2.Managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.example.brickses2.Constants.OpenGLConstants;
import com.example.brickses2.Constants.WorldConstants;

/**
 * Created by Олег on 21.11.2015.
 */
public final class TextureManager {

    public static int[] texturenames = new int[OpenGLConstants.TEXTURES_COUNT];
    private static short textureIndex = 0;

    public static void BindTexture(String texturePath, Context mContext)
    {
        GLES20.glGenTextures(OpenGLConstants.TEXTURES_COUNT, texturenames, 0);

        // Retrieve our image from resources.
        int id = mContext.getResources().getIdentifier(texturePath, null, mContext.getPackageName());

        // Temporary create a bitmap
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), id);

        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureIndex);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[textureIndex]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();
        textureIndex++;
    }

    public static void Reset() {
        textureIndex = 0;
    }
}
