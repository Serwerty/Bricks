package com.example.brickses2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.view.MotionEvent;

import com.example.brickses2.World.World;

public class GLRenderer implements Renderer {

	// Our matrices
	private final float[] mtrxProjection = new float[16];
	private final float[] mtrxView = new float[16];
	private final float[] mtrxProjectionAndView = new float[16];
	
	// Geometric variables
	public static float vertices[];
	public static short indices[];
	public FloatBuffer vertexBuffer;
	public ShortBuffer drawListBuffer;
	private Ball ball;
	// Our screenresolution
	float	mScreenWidth = 1280;
	float	mScreenHeight = 768;

	// Misc
	Context mContext;
	long mLastTime;
	int mProgram;
	boolean isLeftSide=false;
	boolean isRightSide=false;


	public Rect player;
	
	public GLRenderer(Context c)
	{
		mContext = c;
		mLastTime = System.currentTimeMillis() + 100;
	}
	
	public void onPause()
	{
		/* Do stuff to pause the renderer */
	}
	
	public void onResume()
	{
		/* Do stuff to resume the renderer */
		mLastTime = System.currentTimeMillis();
	}
	
	@Override
	public void onDrawFrame(GL10 unused) {
		
		// Get the current time
    	long now = System.currentTimeMillis();
    	
    	// We should make sure we are valid and sane
    	if (mLastTime > now) return;
        
    	// Get the amount of time the last frame took.
    	long elapsed = now - mLastTime;
		
		// Update our example
		World.GetInstance().DrawWorld();
		MovePlayer();
		ball.MoveBall(player.left);

		// Render our example
		Render(mtrxProjectionAndView);
		
		// Save the current time to see how long it took :).
        mLastTime = now;
		
	}
	
	private void Render(float[] m) {
		
		// clear Screen and Depth Buffer, we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        // get handle to vertex shader's vPosition member
	    int mPositionHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_SolidColor, "vPosition");
	    
	    // Enable generic vertex attribute array
	    GLES20.glEnableVertexAttribArray(mPositionHandle);

	    // Prepare the triangle coordinate data
	    GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, World.GetInstance().vertexBuffer);


	    // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(riGraphicTools.sp_SolidColor, "uMVPMatrix");

        // Apply the projection and view transforzmation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, World.GetInstance().size1,
				GLES20.GL_UNSIGNED_SHORT, World.GetInstance().drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);

		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Prepare the triangle coordinate data
		GLES20.glVertexAttribPointer(mPositionHandle, 3,
				GLES20.GL_FLOAT, false,
				0, vertexBuffer);


		// Get handle to shape's transformation matrix
		mtrxhandle = GLES20.glGetUniformLocation(riGraphicTools.sp_SolidColor, "uMVPMatrix");

		// Apply the projection and view transformation
		GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

		// Draw the triangle
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length,
				GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(mPositionHandle);

		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Prepare the triangle coordinate data
		GLES20.glVertexAttribPointer(mPositionHandle, 3,
				GLES20.GL_FLOAT, false,
				0, ball.vertexBuffer);


		// Get handle to shape's transformation matrix
		mtrxhandle = GLES20.glGetUniformLocation(riGraphicTools.sp_SolidColor, "uMVPMatrix");

		// Apply the projection and view transformation
		GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

		// Draw the triangle
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, ball.indices.length,
				GLES20.GL_UNSIGNED_SHORT, ball.drawListBuffer);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(mPositionHandle);


	}
	

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		// We need to know the current width and height.
		mScreenWidth = width;
		mScreenHeight = height;
		
		// Redo the Viewport, making it fullscreen.
		GLES20.glViewport(0, 0, (int) mScreenWidth, (int) mScreenHeight);
		
		// Clear our matrices
	    for(int i=0;i<16;i++)
	    {
	    	mtrxProjection[i] = 0.0f;
	    	mtrxView[i] = 0.0f;
	    	mtrxProjectionAndView[i] = 0.0f;
	    }
	    
	    // Setup our screen width and height for normal sprite translation.
	    Matrix.orthoM(mtrxProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);

		// Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		ball = new Ball();
		// Create the triangle
		SetupTriangle();
		World.GetInstance().DrawWorld();
		ball.MoveBall(player.left);

		// Set the clear color to black
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);	

	    // Create the shaders
	    int vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_SolidColor);
	    int fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_SolidColor);

	    riGraphicTools.sp_SolidColor = GLES20.glCreateProgram();             // create empty OpenGL ES Program
	    GLES20.glAttachShader(riGraphicTools.sp_SolidColor, vertexShader);   // add the vertex shader to program
	    GLES20.glAttachShader(riGraphicTools.sp_SolidColor, fragmentShader); // add the fragment shader to program
	    GLES20.glLinkProgram(riGraphicTools.sp_SolidColor);                  // creates OpenGL ES program executables
	    
	    // Set our shader programm
		GLES20.glUseProgram(riGraphicTools.sp_SolidColor);
	}
	
	public void SetupTriangle()
	{
		player = new Rect();
		player.left = 0;
		player.right = 300;
		player.bottom = 0;
		player.top = 80;

		vertices = new float[]
				{player.left, player.top, 0.0f,
						player.left, player.bottom, 0.0f,
						player.right, player.bottom, 0.0f,
						player.right, player.top, 0.0f,
				};

		// We have to create the vertices of our triangle.
		/*vertices = new float[]
		           {10.0f, 200f, 0.0f,
					10.0f, 100f, 0.0f,
					500f, 100f, 0.0f,
					500f, 200f, 0.0f
		           };
		*/
		indices = new short[] {0, 1, 2, 0, 2, 3 }; // The order of vertexrendering.

		// The vertex buffer.
		ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		// initialize byte buffer for the draw list
		ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(indices);
		drawListBuffer.position(0);

	}

	public void processTouchEvent(MotionEvent event)
	{
		int screenhalf = (int) (mScreenWidth / 2);
		// Get the half of screen value
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			if(event.getX()<screenhalf) {
				isLeftSide = true;
				isRightSide = false;
			}
			else if (event.getX()>=screenhalf) {
				isLeftSide = false;
				isRightSide = true;
			}
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			isRightSide=false;
			isLeftSide=false;
		}

		// Update the new data.

	}

	private void MovePlayer()
	{
		if(isLeftSide && player.left>10)
		{
			player.left -= 5;
			player.right -= 5;
		}
		else if (isRightSide  && player.right<=mScreenWidth-10)
		{
			player.left += 5;
			player.right += 5;
		}
		TranslateSprite();
	}

	public void TranslateSprite()
	{
		vertices = new float[]
				{player.left, player.top, 0.0f,
						player.left, player.bottom, 0.0f,
						player.right, player.bottom, 0.0f,
						player.right, player.top, 0.0f,
				};
		// The vertex buffer.
		ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}
}
