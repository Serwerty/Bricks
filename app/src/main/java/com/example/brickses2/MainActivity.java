package com.example.brickses2;


import android.content.Intent;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.brickses2.GLClasses.GLSurface;

import java.nio.IntBuffer;

public class MainActivity extends Activity {

	// Our OpenGL Surfaceview
	public GLSurfaceView glSurfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// Turn off the window's title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        // Super
		super.onCreate(savedInstanceState);
		
		// Fullscreen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		IntBuffer i = IntBuffer.allocate(1);
		GLES20.glGetIntegerv(GLES20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS, i);

        // We create our Surfaceview for our OpenGL here.
        glSurfaceView = new GLSurface(this);

        // Set our view.	
		setContentView(R.layout.activity_main);
		
		// Retrieve our Relative layout from our main layout we just set to our view.
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.gamelayout);
        
        // Attach our surfaceview to our relative layout from our main layout.
        RelativeLayout.LayoutParams glParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.addView(glSurfaceView, glParams);
	}

	@Override
	protected void onPause() {
		super.onPause();
		glSurfaceView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		glSurfaceView.onResume();
	}

}