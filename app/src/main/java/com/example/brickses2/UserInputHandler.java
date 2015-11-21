package com.example.brickses2;

import android.view.MotionEvent;

import com.example.brickses2.GLClasses.GLRenderer;
import com.example.brickses2.GameObjects.PlayerObject;
import com.example.brickses2.GameObjects.World;

import org.w3c.dom.UserDataHandler;

/**
 * Created by Олег on 20.11.2015.
 */
public final class UserInputHandler {

    public static void processTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE)
        {
            PlayerObject _player = (PlayerObject)World.GetInstance().graphicEntities.get(0);
            _player.SetPosition(event.getX());
        }

        // Update the new data.

    }

}
