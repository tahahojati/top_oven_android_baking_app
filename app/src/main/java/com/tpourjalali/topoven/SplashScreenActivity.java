package com.tpourjalali.topoven;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tpourjalali.topoven.helpers.RepoUpdateTask;
import com.tpourjalali.topoven.model.RecipeRepo;

import java.util.Calendar;
import java.util.Date;

/**
 * If data too old, Creates an intent service to download the JSON from internet, service saves the json to sharedpreferences(it may or may not fail).
 * Gets the data from sharedpreferences, if failure occurs, shows a notification and quits. else, starts main activity.
 */
public class SplashScreenActivity extends AppCompatActivity{
    private RecipeRepo mRepo;
    private static final int DOWNLOAD_JOB_ID = 1000;
    private boolean mResumed = false, mUpdateCompleted = false;
    private static final String TAG = "SplashScreenActivity";
    private static final IntentFilter UPDATE_INTENT_FILTER = new IntentFilter();
    static{
        UPDATE_INTENT_FILTER.addAction(RepoUpdateTask.RESULT_ACTION);
    }

    private Intent mUpdateResultIntent;
    private BroadcastReceiver mReceiver = new DownloadResultReceiver();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        //check if you need to update the repo,
        //create an intent service that calls RecipeRepo.updateRepo()
        //set up a broadcast receiver that listens to the service.
        //check if number of recipe's is greater than 0 if so go to the next activity.
        setContentView(R.layout.activity_splash_screen);
        mRepo = RecipeRepo.getInstance(getApplicationContext());
        Date lastUpdate = mRepo.getLastUpdateDate();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        boolean internet = lastUpdate == null || lastUpdate.before(cal.getTime());
        Intent serviceIntet = RepoUpdateTask.createIntent(internet);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiver, UPDATE_INTENT_FILTER);
        RepoUpdateTask.enqueueWork(this, RepoUpdateTask.class, DOWNLOAD_JOB_ID, serviceIntet);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mUpdateCompleted) // in case we have come back from background and update has completed in background.
            finishActivityAfterUpdate();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private void finishActivityAfterUpdate() {
        if(mUpdateResultIntent.getBooleanExtra(RepoUpdateTask.SUCCESS, false))
            Toast.makeText(this, getString(R.string.error_internet_download), Toast.LENGTH_LONG).show();
        if(mRepo.getRecipeCount() != 0){
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
    private class DownloadResultReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            mUpdateResultIntent = intent;
            mUpdateCompleted = true;
            if(mResumed){
                finishActivityAfterUpdate();
            }
        }
    }


}
