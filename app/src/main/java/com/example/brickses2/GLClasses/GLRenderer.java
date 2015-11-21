package com.example.brickses2.GLClasses;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.view.MotionEvent;

import com.example.brickses2.Constants.WorldConstants;
import com.example.brickses2.GameObjects.World;
import com.example.brickses2.Managers.BufferCollection;
import com.example.brickses2.Managers.BufferManager;
import com.example.brickses2.Managers.TextureManager;

public class GLRenderer implements Renderer {

	// Our matrices
	private final float[] mtrxProjection = new float[16];
	private final float[] mtrxView = new float[16];
	private final float[] mtrxProjectionAndView = new float[16];
	
	public static int screenWidth = 1280;
	public static int screenHeight = 720;
	public static float uvs[];
	public FloatBuffer uvBuffer;
	Context mContext;


	public GLRenderer(Context c) {
	mContext = c;
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
	    int mPositionHandle = GLES20.glGetAttribLocation(ShaderTools.sp_Image, "vPosition");
	    
	    // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(ShaderTools.sp_Image, "uMVPMatrix");

        // Apply the projection and view transforzmation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

		// Enable generic vertex attribute array
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Get handle to texture coordinates location and load the texture uvs
		int mTexCoordLoc = GLES20.glGetAttribLocation(ShaderTools.sp_Image, "a_texCoord" );

		GLES20.glEnableVertexAttribArray(mTexCoordLoc);

		int mSamplerLoc = GLES20.glGetUniformLocation(ShaderTools.sp_Image, "s_texture");


		// Set the sampler texture unit to 0, where we have saved the texture.
		GLES20.glUniform1i(mSamplerLoc, 1);

        BufferManager currentBM = BufferManager.GetInstance();
		RenderGraphicsBuffer(mPositionHandle, mTexCoordLoc,
				currentBM.BallBufferCollection.vertexBuffer,
				currentBM.BallBufferCollection.drawListBuffer,
				currentBM.BallBufferCollection.uvBuffer,
				currentBM.BallBufferCollection.indicesCount);

		GLES20.glUniform1i(mSamplerLoc, 0);

		RenderGraphicsBuffer(mPositionHandle, mTexCoordLoc,
				currentBM.BricksBufferCollection.vertexBuffer,
				currentBM.BricksBufferCollection.drawListBuffer,
				currentBM.BricksBufferCollection.uvBuffer,
				currentBM.BricksBufferCollection.indicesCount);

		GLES20.glUniform1i(mSamplerLoc, 2);

		RenderGraphicsBuffer(mPositionHandle, mTexCoordLoc,
				currentBM.PlayerBufferCollection.vertexBuffer,
				currentBM.PlayerBufferCollection.drawListBuffer,
				currentBM.PlayerBufferCollection.uvBuffer,
				currentBM.PlayerBufferCollection.indicesCount);

		GLES20.glDisableVertexAttribArray(mPositionHandle);
		GLES20.glDisableVertexAttribArray(mTexCoordLoc);
	}

	public void RenderGraphicsBuffer(int mPositionHandle,int mTexCoordLoc, FloatBuffer verteces,
									 ShortBuffer indeces, FloatBuffer uvBuffer1, int indecesCount){

		GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, uvBuffer1);

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

	public void SetupImage()
	{


		// 30 imageobjects times 4 vertices times (u and v)
		int size = WorldConstants.COUNT_OF_BRICKS_IN_A_COLUMN *
				WorldConstants.COUNT_OF_BRICKS_IN_A_ROW * 8;
		uvs = new float[size];

		// We will make 30 randomly textures objects
		for(int i=0; i < size / 8; i++)
		{
			int random_u_offset = 0;
			int random_v_offset = 0;

			//uvs = new float[]{0,0,0,1,1,1,1,0};
			 //Adding the UV's using the offsets
			uvs[(i*8) + 0] = 0;
			uvs[(i*8) + 1] = 0;
			uvs[(i*8) + 2] = 0;
			uvs[(i*8) + 3] = 1;
			uvs[(i*8) + 4] = 1;
			uvs[(i*8) + 5] = 1;
			uvs[(i*8) + 6] = 1;
			uvs[(i*8) + 7] = 0;
		}

		// The texture buffer
		ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
		bb.order(ByteOrder.nativeOrder());
		uvBuffer = bb.asFloatBuffer();
		uvBuffer.put(uvs);
		uvBuffer.position(0);

		// Generate Textures, if more needed, alter these numbers.
		int[] texturenames = new int[1];
		GLES20.glGenTextures(1, texturenames, 0);

		// Retrieve our image from resources.
		int id = mContext.getResources().getIdentifier("drawable/file", null, mContext.getPackageName());

		// Temporary create a bitmap
		Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), id);

		// Bind texture to texturename
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);

		// Set filtering
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

		// Load the bitmap into the bound texture.
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

		// We are done using the bitmap so we should recycle it.
		bmp.recycle();

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {


		World.GetInstance().DrawWorld();
		//SetupImage();
		TextureManager.BindTexture("drawable/brick", mContext);
		TextureManager.BindTexture("drawable/file", mContext);
		TextureManager.BindTexture("drawable/brick", mContext);
		// Set the clear color to black
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		// Create the shaders, images
		int vertexShader = ShaderTools.loadShader(GLES20.GL_VERTEX_SHADER, ShaderTools.vs_Image);
		int fragmentShader = ShaderTools.loadShader(GLES20.GL_FRAGMENT_SHADER, ShaderTools.fs_Image);

		ShaderTools.sp_Image = GLES20.glCreateProgram();             // create empty OpenGL ES Program
		GLES20.glAttachShader(ShaderTools.sp_Image, vertexShader);   // add the vertex shader to program
		GLES20.glAttachShader(ShaderTools.sp_Image, fragmentShader); // add the fragment shader to program
		GLES20.glLinkProgram(ShaderTools.sp_Image);                  // creates OpenGL ES program executables
		GLES20.glUseProgram(ShaderTools.sp_Image);

	   /* Create the shaders
	    vertexShader = ShaderTools.loadShader(GLES20.GL_VERTEX_SHADER, ShaderTools.vs_SolidColor);
		fragmentShader = ShaderTools.loadShader(GLES20.GL_FRAGMENT_SHADER, ShaderTools.fs_SolidColor);

	    ShaderTools.sp_SolidColor = GLES20.glCreateProgram();             // create empty OpenGL ES Program
	    GLES20.glAttachShader(ShaderTools.sp_SolidColor, vertexShader);   // add the vertex shader to program
	    GLES20.glAttachShader(ShaderTools.sp_SolidColor, fragmentShader); // add the fragment shader to program
	    GLES20.glLinkProgram(ShaderTools.sp_SolidColor);                  // creates OpenGL ES program executables

	    // Set our shader programm
		//GLES20.glUseProgram(ShaderTools.sp_SolidColor);*/

	}

}
