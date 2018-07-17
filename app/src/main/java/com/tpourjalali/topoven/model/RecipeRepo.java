package com.tpourjalali.topoven.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public final class RecipeRepo {
    private static RecipeRepo sInstance;
    private static final String TAG = "RecipeRepo";
    private Context mContext;
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private Retrofit mRetrofit;
    private RecipeInternetService mRecipeInternetService;
    private List<Recipe> mRecipeList = null;
    private final File mDataFile;

    /**
     * first call to this function may be long running.
     * @param applicationContext
     * @return
     */
    synchronized public static RecipeRepo getInstance(Context applicationContext){
        if(sInstance == null){
            sInstance = new RecipeRepo(applicationContext.getApplicationContext());
        }
        return sInstance;
    }
    private RecipeRepo(Context applicationContext) {
        mContext = applicationContext.getApplicationContext();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mRecipeInternetService = mRetrofit.create(RecipeInternetService.class);
        mDataFile = new File(mContext.getFilesDir(), "recipes");
    }
    public Date getLastUpdateDate(){return null;}

    /**
     * This function will take a long time to execute. Run off the main thread.
     */
    public void updateRepo(){
        //check if we are online.
        boolean online = true;
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm == null)
            online = false;
        else {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if(info == null)
                online = false;
            else
                online = info.isConnectedOrConnecting();
        }

        if(online){
            updateRepoFromInternet();
            saveRepoToDisk();
        } else {
            loadRepoFromDisk();
        }
    }

    private void updateRepoFromInternet() {
        try {
            Response<List<Recipe>> response = mRecipeInternetService.getRecipeList().execute();
            if(response.isSuccessful()){
                mRecipeList = response.body();
            } else {
                Log.d(TAG, "Download of recipes was not successfull " + response.code() +": "+response.errorBody());
            }
        } catch (IOException e) {
            Log.d(TAG, "Failed to download recipes from internet", e);
        }
    }

    private void saveRepoToDisk() {
        if(mRecipeList == null || mRecipeList.size() == 0)
            return;
        try(
                FileOutputStream fos = new FileOutputStream(mDataFile, false);
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ){
            oos.writeObject(mRecipeList);
        } catch (IOException e) {
            Log.e(TAG, "failed to open the repo file for writing: "+mDataFile.getAbsolutePath(), e);
        }
    }

    private void loadRepoFromDisk() {
        try(
                FileInputStream fis = new FileInputStream(mDataFile);
                ObjectInputStream ois = new ObjectInputStream(fis)
        ){
            Object object = ois.readObject();
            if(object != null && object instanceof List )
                mRecipeList = (List<Recipe>) object;
        } catch (IOException e) {
            Log.e(TAG, "failed to open the repo file for reading: "+mDataFile.getAbsolutePath(), e);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "failed to convert contents of repo file to object "+mDataFile.getAbsolutePath(), e);
        }
    }

    //    public String getJson(){return null;}
    @NonNull
    public List<Recipe> getRecipeList(){
        if(mRecipeList == null){
            return new ArrayList<>();
        }
        return mRecipeList;
    }
    public Recipe getRecipe(int index){
        return mRecipeList.get(index);
    }
    public int getRecipeCount(){
        if(mRecipeList == null)
            return 0;
        else
            return mRecipeList.size();
    }
    public interface RecipeInternetService{
        @GET
        Call<List<Recipe>> getRecipeList();
    }

}
