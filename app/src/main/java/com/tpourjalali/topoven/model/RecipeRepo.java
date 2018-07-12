package com.tpourjalali.topoven.model;

import android.content.Context;

import java.util.Date;
import java.util.List;

public final class RecipeRepo {
    private static RecipeRepo sInstance;
    private Context mContext;

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
        mContext = applicationContext;
    }
    public Date getLastUpdateDate(){return null;}
    public void updateRepo(){}
    public String getJson(){return null;}
    public List<Recipe> getRecipeList(){return null;}
    public Recipe getRecipe(int index){return null;}
    public int getRecipeCount(){return 0;}

}
