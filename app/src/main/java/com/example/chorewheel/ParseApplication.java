package com.example.chorewheel;

import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();


        // TESTING ONLY - TO BE REMOVED
        //Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        //OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        //httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //builder.networkInterceptors().add(httpLoggingInterceptor);

        // This should be moved to server side code if possible
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("LhKZqn1Q7IbCpKCz2bOJNigBF9OIF2HIMdCV0bvg")
                .clientKey("hZgY8cljPXVK0yzogHdEMSZVSRAC4t9GjLMpq7Od")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
