package com.tpourjalali.topoven;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.tpourjalali.topoven.model.Recipe;
import com.tpourjalali.topoven.model.RecipeRepo;

import java.util.List;

public class BakingWidgetRemoteViewsService extends RemoteViewsService {
    public static final String KEY_RECIPE_INDEX = "recipe_index";
    public static final String KEY_RECIPE_INGREDIENTS = "ingredients";
    private static final String TAG = BakingWidgetRemoteViewsService.class.getName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "onGetViewFactory was called");
        return new BakingWidgetRemoteViewsFactory(getApplicationContext());
    }
}

class BakingWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mApplicationContext;
    private RecipeRepo mRepo;
    private static final String TAG = BakingWidgetRemoteViewsFactory.class.getName();
    private List<Recipe> mRecipeList;

    public BakingWidgetRemoteViewsFactory(Context applicationContext){
        mApplicationContext = applicationContext;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate was called");
        mRepo = RecipeRepo.getInstance(mApplicationContext);
        mRepo.updateRepo(false);
    }

    @Override
    public void onDataSetChanged() {
        mRecipeList = mRepo.getRecipeList();
        Log.d(TAG, "onDataSetChanged was called");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy was called");

    }

    @Override
    public int getCount() {
        Log.d(TAG, "getCount returning "+mRecipeList.size());
        return mRecipeList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews V = new RemoteViews(mApplicationContext.getPackageName(), R.layout.recipe_list_item);
        Recipe recipe = mRecipeList.get(position);
        V.setTextViewText(R.id.recipe_title, recipe.getName());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(BakingWidgetRemoteViewsService.KEY_RECIPE_INDEX, position);
        fillInIntent.putExtra(BakingWidgetRemoteViewsService.KEY_RECIPE_INGREDIENTS, TextUtils.join(", ", recipe.getIngredients()));
        V.setOnClickFillInIntent(V.getLayoutId(), fillInIntent);
        Log.d(TAG, "getViewAt was called");
        return V;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}