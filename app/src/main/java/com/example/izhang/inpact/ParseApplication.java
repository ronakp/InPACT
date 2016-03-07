package com.example.izhang.inpact;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by izhang on 9/12/15.
 */
public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "81R8CcJk7OaIVcyjt6KEJoZ6bq4n8fynLVPLka2k", "fEisXyRnlW3bgUgrIsL0sDZGzFOZYlBwHBfSyPWc");
    }
}
