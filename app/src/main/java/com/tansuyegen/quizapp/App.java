package com.tansuyegen.quizapp;

import android.app.Application;
import android.util.Log;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Twitter.initialize(this);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("y0JfGrWdFBPCoWOVgMSzL9KQp", "nJst3CoJ5pL2UvN8iCzzHIikFC7IZL9DWUGiuu4zOA7jHKtkVC"))
                .debug(true)
                .build();
        Twitter.initialize(config);


    }
}
