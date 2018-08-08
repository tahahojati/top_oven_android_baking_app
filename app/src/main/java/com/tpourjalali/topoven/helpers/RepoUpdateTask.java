package com.tpourjalali.topoven.helpers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.tpourjalali.topoven.model.RecipeRepo;

public class RepoUpdateTask extends JobIntentService {
    private static final String TAG = "RepoUpdateTask";
    private static final String KEY_INTERNET = "internet";
    public static final String SUCCESS = "loss";
    public static final String INTERNET_LOAD = "internet_load";
    public static final String RESULT_ACTION = RepoUpdateTask.class.getName()+"."+"result";
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork");
        RecipeRepo repo = RecipeRepo.getInstance(getApplicationContext());
        boolean internet = intent.getBooleanExtra(KEY_INTERNET, false);
        boolean resultInternet = repo.updateRepo(internet);
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getApplicationContext());
        Intent resultIntent = new Intent(RESULT_ACTION);
        resultIntent.putExtra(SUCCESS, internet == resultInternet);
        resultIntent.putExtra(INTERNET_LOAD, resultInternet);
        Log.d(TAG, "sending Broadcast "+resultIntent.toString()+"    "+resultIntent.getExtras().toString());
        lbm.sendBroadcast(resultIntent);
        // sendBroadcast(); //not sure about this function.
    }
    public static Intent createIntent(boolean downloadFromInternet){
        Intent intent = new Intent();
        intent.putExtra(KEY_INTERNET, downloadFromInternet);
//        intent.setClassName(RepoUpdateTask.class.getPackage().getName(), RepoUpdateTask.class.getName());
        return intent;
    }
}
