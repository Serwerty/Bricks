package com.example.brickses2.Managers;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.brickses2.Constants.OpenGLConstants;
import com.example.brickses2.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Олег on 16.12.2015.
 */
public final class MediaManager {

    private static Map<String,MediaPlayer> Sounds = new HashMap<>();

    public static void addSound(String name, int resid, Context mContext) {
        Sounds.put(name, MediaPlayer.create(mContext, R.raw.hitmarker));
    }

    public static void Play(String name){
        if (Sounds.get(name)!=null) {
            MediaPlayer _mp = Sounds.get(name);
            _mp.start();
        }
    }
}
