package com.example.brickses2.GLClasses;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.view.MotionEvent;

import com.example.brickses2.GameObjects.World;
import com.example.brickses2.Managers.BufferCollection;
import com.example.brickses2.Managers.BufferManager;

public class GLRenderer implements Renderer {

	// Our matrices
	private final float[] mtrxProjection = new float[16];
	private final float[] mtrxView = new float[16];
	private final float[] mtrxProjectionAndView = new float[16];
	
	public static int screenWidth = 1280;
	public static int screenHeight = 720;

	public GLRenderer(Context c) {

	}
	
	public void onPause() {
		/* Do stuff to pause the renderer */
	}
	
	public void onResume()	{
		/* Do stuff to resume the renderer */
	}
	
	@Override
	public void onDrawFrame(GL10 unused) {

		// Update our example
		World.GetInstance().MoveObjects();
		World.GetInstance().DrawWorld();

		// Render our example
		Render(mtrxProjectionAndView);

	}
	
	private void Render(float[] m) {
		
		// clear Screen and Depth Buffer, we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        // get handle to vertex shader's vPosition member
	    int mPositionHandle = GLES20.glGetAttribLocation(ShaderTools.sp_SolidColor, "vPosition");
	    
	    // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(ShaderTools.sp_SolidColor, "uMVPMatrix");

        // Apply the projection and view transforzmation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

		// Enable generic vertex attribute array
		GLES20.glEnableVertexAttribArray(mPositionHandle);

        BufferManager currentBM = BufferManager.GetInstance();
        RenderGraphicsBuffer(mPositionHandle,
                currentBM.BallBufferCollection.vertexBuffer,
                currentBM.BallBufferCollection.drawListBuffer,
                currentBM.BallBufferCollection.indicesCount);

		RenderGraphicsBuffer(mPositionHandle,
				currentBM.BricksBufferCollection.vertexBuffer,
				currentBM.BricksBufferCollection.drawListBuffer,
				currentBM.BricksBufferCollection.indicesCount);

		RenderGraphicsBuffer(mPositionHandle,
				currentBM.PlayerBufferCollection.vertexBuffer,
				currentBM.PlayerBufferCollection.drawListBuffer,
				currentBM.PlayerBufferCollection.indicesCount);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}

	public void RenderGraphicsBuffer(int mPositionHandle, FloatBuffer verteces,
									 ShortBuffer indeces, int indecesCount){
		// Prepare the triangle coordinate data
		GLES20.glVertexAttribPointer(mPositionHandle, 3,
				GLES20.GL_FLOAT, false,
				0, verteces);

		GLES20.glDrawElements(GLES20.GL_TRIANGLES, indecesCount,
				GLES20.GL_UNSIGNED_SHORT, indeces);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		// We need to know the current width and height.
		screenWidth = width;
		screenHeight = height;
		
		// Redo the Viewport, making it fullscreen.
		GLES20.glViewport(0, 0, screenWidth, screenHeight);
		
		// Clear our matrices
	    for(short i = 0; i < 16; i++)  {
	    	mtrxProjection[i] = 0.0f;
	    	mtrxView[i] = 0.0f;
	    	mtrxProjectionAndView[i] = 0.0f;
	    }
	    
	    // Setup our screen width and height for normal sprite translation.
	    Matrix.orthoM(mtrxProjection, 0, 0f, screenWidth, 0.0f, screenHeight, 0, 50);

		// Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {


		World.GetInstance().DrawWorld();

		// Set the clear color to black
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);	

	    // Create the shaders
	    int vertexShader = ShaderTools.loadShader(GLES20.GL_VERTEX_SHADER, ShaderTools.vs_SolidColor);
	    int fragmentShader = ShaderTools.loadShader(GLES20.GL_FRAGMENT_SHADER, ShaderTools.fs_SolidColor);

	    ShaderTools.sp_SolidColor = GLES20.glCreateProgram();             // create empty OpenGL ES Program
	    GLES20.glAttachShader(ShaderTools.sp_SolidColor, vertexShader);   // add the vertex shader to program
	    GLES20.glAttachShader(ShaderTools.sp_SolidColor, fragmentShader); // add the fragment shader to program
	    GLES20.glLinkProgram(ShaderTools.sp_SolidColor);                  // creates OpenGL ES program executables
	    
	    // Set our shader programm
		GLES20.glUseProgram(ShaderTools.sp_SolidColor);

	}

}
