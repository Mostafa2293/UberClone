package com.example.uberclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("c9vT3spcOwI2ybXr8UBpzBMZGB6J2sbBulwSa39T")
                // if defined
                .clientKey("1R0s3G3UPI4dLCrqomaYTsyUnnaTIQYlY313J6SE")
                .server("https://parseapi.back4app.com/")
                .build()
        );

    }
}
