package com.blackflag.myhomeassistance;

import android.app.Application;

import net.gotev.speech.Logger;
import net.gotev.speech.Speech;

/**
 * Created by BlackFlag on 2/21/2017.
 */

public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Speech.init(this, getPackageName());
        Logger.setLogLevel(Logger.LogLevel.DEBUG);
    }
}
