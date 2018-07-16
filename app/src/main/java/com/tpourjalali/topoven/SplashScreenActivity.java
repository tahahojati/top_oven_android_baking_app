package com.tpourjalali.topoven;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * If data too old, Creates an intent service to download the JSON from internet, service saves the json to sharedpreferences(it may or may not fail).
 * Gets the data from sharedpreferences, if failure occurs, shows a notification and quits. else, starts main activity.
 */
public class SplashScreenActivity extends AppCompatActivity{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        //check if you need to update the repo,
        //create an intent service that calls RecipeRepo.updateRepo()
        //set up a broadcast receiver that listens to the service.
        //check if number of recipe's is greater than 0 if so go to the next activity.
        setContentView(R.layout.activity_splash_screen);
    }
}
