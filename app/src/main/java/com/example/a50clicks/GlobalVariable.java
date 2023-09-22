package com.example.a50clicks;

import android.app.Application;
import android.content.SharedPreferences;

public class GlobalVariable extends Application {

    private static float bestTime = Integer.MAX_VALUE;


    public static float getBestTime() {
        return bestTime;
    }

    public static void setBestTime(float bestTime) {
        GlobalVariable.bestTime = bestTime;

    }
}
